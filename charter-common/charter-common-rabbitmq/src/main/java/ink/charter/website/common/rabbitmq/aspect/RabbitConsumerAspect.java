package ink.charter.website.common.rabbitmq.aspect;

import ink.charter.website.common.rabbitmq.annotation.RabbitConsumer;
import ink.charter.website.common.rabbitmq.constant.RabbitMQConstant;
import ink.charter.website.common.rabbitmq.service.RabbitMQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.ImmediateRequeueAmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ消费者切面
 * 处理@RabbitConsumer注解，实现队列和交换机的自动创建和绑定
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@Aspect
@Order(50)
@Component
@RequiredArgsConstructor
public class RabbitConsumerAspect implements InitializingBean {

    private final RabbitMQService rabbitMQService;
    private final RabbitAdmin rabbitAdmin;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 在Bean初始化后扫描所有@RabbitConsumer注解的方法
        // 这里可以实现自动创建队列和交换机的逻辑
        log.info("RabbitConsumerAspect初始化完成");
    }

    @Around("@annotation(rabbitConsumer)")
    public Object around(ProceedingJoinPoint joinPoint, RabbitConsumer rabbitConsumer) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        try {
            // 确保队列和交换机存在
            ensureQueueAndExchangeExist(rabbitConsumer, joinPoint);
            
            // 检查消费条件
            if (!shouldConsumeMessage(joinPoint, rabbitConsumer)) {
                log.debug("消费条件不满足，跳过消息处理: method={}", method.getName());
                return null;
            }
            
            // 执行原方法（消费逻辑）
            Object result = joinPoint.proceed();
            
            log.debug("消息消费成功: method={}, queue={}", method.getName(), rabbitConsumer.queue());
            return result;
            
        } catch (Exception e) {
            // 处理消费失败
            handleConsumeFailure(e, rabbitConsumer, joinPoint);
            throw e;
        }
    }

    /**
     * 确保队列和交换机存在
     */
    private void ensureQueueAndExchangeExist(RabbitConsumer rabbitConsumer, ProceedingJoinPoint joinPoint) {
        try {
            String queueName = parseExpression(rabbitConsumer.queue(), joinPoint, null);
            String exchangeName = parseExpression(rabbitConsumer.exchange(), joinPoint, null);
            String routingKey = parseExpression(rabbitConsumer.routingKey(), joinPoint, null);
            
            // 创建交换机（如果指定了交换机名称）
            if (StringUtils.hasText(exchangeName)) {
                createExchange(exchangeName, rabbitConsumer.exchangeType());
            }
            
            // 创建队列
            createQueue(queueName, rabbitConsumer);
            
            // 绑定队列到交换机
            if (StringUtils.hasText(exchangeName) && StringUtils.hasText(routingKey)) {
                rabbitMQService.bindQueue(queueName, exchangeName, routingKey);
                log.debug("队列绑定成功: queue={}, exchange={}, routingKey={}", queueName, exchangeName, routingKey);
            }
            
        } catch (Exception e) {
            log.error("创建队列和交换机失败: queue={}, exchange={}", rabbitConsumer.queue(), rabbitConsumer.exchange(), e);
            throw new RuntimeException("队列和交换机创建失败", e);
        }
    }

    /**
     * 创建交换机
     */
    private void createExchange(String exchangeName, RabbitConsumer.ExchangeType exchangeType) {
        try {
            switch (exchangeType) {
                case DIRECT:
                    rabbitMQService.declareDirectExchange(exchangeName, true, false);
                    break;
                case TOPIC:
                    rabbitMQService.declareTopicExchange(exchangeName, true, false);
                    break;
                case FANOUT:
                    rabbitMQService.declareFanoutExchange(exchangeName, true, false);
                    break;
                case HEADERS:
                    // Headers交换机需要特殊处理
                    HeadersExchange headersExchange = new HeadersExchange(exchangeName, true, false);
                    rabbitAdmin.declareExchange(headersExchange);
                    break;
                default:
                    log.warn("不支持的交换机类型: {}", exchangeType);
                    break;
            }
            log.debug("交换机创建成功: name={}, type={}", exchangeName, exchangeType);
        } catch (Exception e) {
            log.error("创建交换机失败: name={}, type={}", exchangeName, exchangeType, e);
            throw new RuntimeException("交换机创建失败", e);
        }
    }

    /**
     * 创建队列
     */
    private void createQueue(String queueName, RabbitConsumer rabbitConsumer) {
        try {
            Map<String, Object> arguments = parseQueueArguments(rabbitConsumer.queueArguments());
            
            // 添加死信队列配置
            if (rabbitConsumer.enableDlx()) {
                arguments.put(RabbitMQConstant.DEAD_LETTER_EXCHANGE_PARAM, rabbitConsumer.dlxExchange());
                arguments.put(RabbitMQConstant.DEAD_LETTER_ROUTING_KEY_PARAM, rabbitConsumer.dlxRoutingKey());
            }
            
            // 创建队列
            rabbitMQService.declareQueue(queueName, rabbitConsumer.durable(), 
                    rabbitConsumer.exclusive(), rabbitConsumer.autoDelete(), arguments);
            
            log.debug("队列创建成功: name={}, durable={}, exclusive={}, autoDelete={}", 
                    queueName, rabbitConsumer.durable(), rabbitConsumer.exclusive(), rabbitConsumer.autoDelete());
        } catch (Exception e) {
            log.error("创建队列失败: name={}", queueName, e);
            throw new RuntimeException("队列创建失败", e);
        }
    }

    /**
     * 解析队列参数
     */
    private Map<String, Object> parseQueueArguments(String[] queueArguments) {
        Map<String, Object> arguments = new HashMap<>();
        
        for (String arg : queueArguments) {
            if (StringUtils.hasText(arg) && arg.contains("=")) {
                String[] parts = arg.split("=", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();
                
                // 尝试转换为合适的类型
                Object convertedValue = convertArgumentValue(key, value);
                arguments.put(key, convertedValue);
            }
        }
        
        return arguments;
    }

    /**
     * 转换参数值为合适的类型
     */
    private Object convertArgumentValue(String key, String value) {
        try {
            // 根据参数名称判断类型
            switch (key) {
                case RabbitMQConstant.MESSAGE_TTL:
                case RabbitMQConstant.QUEUE_TTL:
                    return Long.parseLong(value);
                case RabbitMQConstant.MAX_LENGTH:
                    return Integer.parseInt(value);
                case RabbitMQConstant.MAX_LENGTH_BYTES:
                    return Long.parseLong(value);
                default:
                    // 尝试解析为数字
                    if (value.matches("\\d+")) {
                        return Long.parseLong(value);
                    }
                    // 尝试解析为布尔值
                    if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                        return Boolean.parseBoolean(value);
                    }
                    // 默认返回字符串
                    return value;
            }
        } catch (NumberFormatException e) {
            log.warn("参数值转换失败，使用字符串类型: key={}, value={}", key, value);
            return value;
        }
    }

    /**
     * 检查是否应该消费消息
     */
    private boolean shouldConsumeMessage(ProceedingJoinPoint joinPoint, RabbitConsumer rabbitConsumer) {
        if (!StringUtils.hasText(rabbitConsumer.condition())) {
            return true;
        }
        
        try {
            EvaluationContext context = createEvaluationContext(joinPoint, null);
            Expression expression = parser.parseExpression(rabbitConsumer.condition());
            Boolean shouldConsume = expression.getValue(context, Boolean.class);
            return shouldConsume != null && shouldConsume;
        } catch (Exception e) {
            log.warn("解析消费条件失败，默认消费消息: condition={}", rabbitConsumer.condition(), e);
            return true;
        }
    }

    /**
     * 处理消费失败
     */
    private void handleConsumeFailure(Exception e, RabbitConsumer rabbitConsumer, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        
        switch (rabbitConsumer.failureStrategy()) {
            case RETRY_AND_DLX:
                log.error("消息消费失败，将重试后发送到死信队列: method={}", methodName, e);
                handleRetryAndDlx(joinPoint, rabbitConsumer, methodName, e);
                break;
                
            case DIRECT_DLX:
                log.error("消息消费失败，直接发送到死信队列: method={}", methodName, e);
                sendToDeadLetterQueue(joinPoint.getArgs(), rabbitConsumer, methodName, e.getMessage());
                break;
                
            case DISCARD:
                log.error("消息消费失败，丢弃消息: method={}", methodName, e);
                break;
                
            case REQUEUE:
                log.error("消息消费失败，重新入队: method={}", methodName, e);
                handleRequeue(rabbitConsumer, methodName, e);
                break;
                
            case CUSTOM:
                log.error("消息消费失败，需要自定义处理: method={}", methodName, e);
                handleCustomFailure(joinPoint, rabbitConsumer, methodName, e);
                break;
                
            default:
                log.error("消息消费失败，未知处理策略: method={}, strategy={}", 
                        methodName, rabbitConsumer.failureStrategy(), e);
                break;
        }
    }

    /**
     * 发送到死信队列
     */
    private void sendToDeadLetterQueue(Object[] args, RabbitConsumer rabbitConsumer, String methodName, String errorMessage) {
        try {
            // 获取原始消息内容
            Object messageContent = null;
            if (args != null && args.length > 0) {
                messageContent = args[0]; // 通常第一个参数是消息内容
            }
            
            // 构建死信消息
            Map<String, Object> dlxMessage = new HashMap<>();
            dlxMessage.put("originalMessage", messageContent);
            dlxMessage.put("originalQueue", rabbitConsumer.queue());
            dlxMessage.put("failureReason", errorMessage);
            dlxMessage.put("failureTime", System.currentTimeMillis());
            dlxMessage.put("consumerMethod", methodName);
            
            // 构建消息头
            Map<String, Object> headers = new HashMap<>();
            headers.put("originalQueue", rabbitConsumer.queue());
            headers.put("originalMethod", methodName);
            headers.put("failureReason", errorMessage);
            headers.put("failureTime", System.currentTimeMillis());
            headers.put(RabbitMQConstant.HEADER_RETRY_COUNT, 0);
            
            // 发送到死信队列
            rabbitMQService.send(rabbitConsumer.dlxExchange(), rabbitConsumer.dlxRoutingKey(), dlxMessage, headers);
            
            log.info("消息已发送到死信队列: dlxExchange={}, dlxRoutingKey={}, originalQueue={}", 
                    rabbitConsumer.dlxExchange(), rabbitConsumer.dlxRoutingKey(), rabbitConsumer.queue());
        } catch (Exception e) {
            log.error("发送到死信队列失败: method={}, error={}", methodName, e.getMessage(), e);
        }
    }

    /**
     * 解析SpEL表达式
     */
    private String parseExpression(String expressionString, ProceedingJoinPoint joinPoint, Object result) {
        if (!StringUtils.hasText(expressionString)) {
            return expressionString;
        }
        
        try {
            EvaluationContext context = createEvaluationContext(joinPoint, result);
            Expression expression = parser.parseExpression(expressionString);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            log.warn("解析SpEL表达式失败，使用原始值: expression={}", expressionString, e);
            return expressionString;
        }
    }

    /**
     * 创建SpEL评估上下文
     */
    private EvaluationContext createEvaluationContext(ProceedingJoinPoint joinPoint, Object result) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        
        if (joinPoint != null) {
            // 添加方法参数
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            
            if (paramNames != null && args != null) {
                for (int i = 0; i < paramNames.length && i < args.length; i++) {
                    context.setVariable(paramNames[i], args[i]);
                }
            }
            
            // 添加方法信息
            context.setVariable("method", method);
            context.setVariable("methodName", method.getName());
            context.setVariable("className", method.getDeclaringClass().getSimpleName());
        }
        
        // 添加方法返回值
        context.setVariable("result", result);
        context.setVariable("returnValue", result);
        
        // 添加当前时间
        context.setVariable("currentTime", System.currentTimeMillis());
        context.setVariable("timestamp", System.currentTimeMillis());
        
        return context;
    }

    /**
     * 处理重试后发送到死信队列策略
     */
    private void handleRetryAndDlx(ProceedingJoinPoint joinPoint, RabbitConsumer rabbitConsumer, String methodName, Exception originalException) {
        int maxRetries = rabbitConsumer.retryCount() > 0 ? rabbitConsumer.retryCount() : RabbitMQConstant.DEFAULT_RETRY_COUNT;
        long retryInterval = rabbitConsumer.retryInterval() > 0 ? rabbitConsumer.retryInterval() : 1000L;
        
        Exception lastException = originalException;
        
        for (int currentRetry = 1; currentRetry <= maxRetries; currentRetry++) {
            try {
                // 等待重试间隔
                if (retryInterval > 0) {
                    Thread.sleep(retryInterval);
                }
                
                log.info("开始第{}次重试消息消费: method={}, maxRetries={}", currentRetry, methodName, maxRetries);
                
                // 重新执行原方法
                joinPoint.proceed();
                
                log.info("消息消费重试成功: method={}, retryCount={}", methodName, currentRetry);
                return; // 重试成功，直接返回
                
            } catch (Throwable throwable) {
                lastException = throwable instanceof Exception ? (Exception) throwable : new RuntimeException(throwable);
                log.warn("消息消费第{}次重试失败: method={}, error={}", currentRetry, methodName, throwable.getMessage());
                
                // 如果不是最后一次重试，继续下一次重试
                if (currentRetry < maxRetries) {
                    continue;
                }
            }
        }
        
        // 所有重试都失败，发送到死信队列
        log.error("消息消费重试全部失败，发送到死信队列: method={}, totalRetries={}", methodName, maxRetries, lastException);
        sendToDeadLetterQueue(joinPoint.getArgs(), rabbitConsumer, methodName, 
                "重试" + maxRetries + "次失败: " + lastException.getMessage() + ", 原始异常: " + originalException.getMessage());
    }

    /**
     * 处理重新入队策略
     */
    private void handleRequeue(RabbitConsumer rabbitConsumer, String methodName, Exception e) {
        try {
            // 检查重试次数，避免无限重新入队
            int maxRetries = rabbitConsumer.retryCount() > 0 ? rabbitConsumer.retryCount() : RabbitMQConstant.DEFAULT_RETRY_COUNT;
            
            // 这里可以通过消息头来跟踪重试次数，但由于在切面中无法直接访问消息头，
            // 我们抛出ImmediateRequeueAmqpException来触发重新入队
            log.warn("消息将重新入队: method={}, maxRetries={}", methodName, maxRetries);
            
            // 抛出ImmediateRequeueAmqpException异常，Spring AMQP会自动处理重新入队
            throw new ImmediateRequeueAmqpException("消息消费失败，重新入队: " + e.getMessage(), e);
            
        } catch (ImmediateRequeueAmqpException requeueException) {
            // 重新抛出，让Spring AMQP处理
            throw requeueException;
        } catch (Exception handleException) {
            log.error("处理重新入队失败: method={}", methodName, handleException);
            throw new RuntimeException("重新入队处理失败", handleException);
        }
    }

    /**
     * TODO 处理自定义失败策略
     */
    private void handleCustomFailure(ProceedingJoinPoint joinPoint, RabbitConsumer rabbitConsumer, String methodName, Exception e) {
        // 这里可以扩展自定义处理逻辑
        // 例如：发送通知、记录特殊日志、调用外部服务等
        
        log.warn("执行自定义失败处理策略: method={}", methodName);
        
        try {
            // 可以根据异常类型进行不同处理
            if (e instanceof IllegalArgumentException) {
                log.error("参数异常，丢弃消息: method={}", methodName, e);
                // 参数异常直接丢弃，不重试
                return;
            } else if (e instanceof RuntimeException) {
                log.error("运行时异常，发送到死信队列: method={}", methodName, e);
                sendToDeadLetterQueue(joinPoint.getArgs(), rabbitConsumer, methodName, e.getMessage());
            } else {
                log.error("未知异常类型，默认发送到死信队列: method={}", methodName, e);
                sendToDeadLetterQueue(joinPoint.getArgs(), rabbitConsumer, methodName, e.getMessage());
            }
            
            // 可以在这里添加更多自定义逻辑
            // 例如：发送邮件通知、调用监控接口等
            
        } catch (Exception customException) {
            log.error("自定义失败处理执行异常: method={}", methodName, customException);
        }
    }
}