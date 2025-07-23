package ink.charter.website.common.rabbitmq.aspect;

import ink.charter.website.common.rabbitmq.annotation.RabbitProducer;
import ink.charter.website.common.rabbitmq.constant.RabbitMQConstant;
import ink.charter.website.common.rabbitmq.service.RabbitMQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
import java.util.concurrent.CompletableFuture;

/**
 * RabbitMQ生产者切面
 * 处理@RabbitProducer注解，实现自动消息发送
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@Aspect
@Order(100)
@Component
@RequiredArgsConstructor
public class RabbitProducerAspect {

    private final RabbitMQService rabbitMQService;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(rabbitProducer)")
    public Object around(ProceedingJoinPoint joinPoint, RabbitProducer rabbitProducer) throws Throwable {
        // 执行原方法
        Object result = joinPoint.proceed();
        
        try {
            // 检查发送条件
            if (!shouldSendMessage(joinPoint, rabbitProducer, result)) {
                return result;
            }
            
            // 解析参数
            String exchange = parseExpression(rabbitProducer.exchange(), joinPoint, result);
            String routingKey = parseExpression(rabbitProducer.routingKey(), joinPoint, result);
            
            // 获取消息内容
            Object message = getMessageContent(joinPoint, rabbitProducer, result);
            
            // 构建消息头
            Map<String, Object> headers = buildHeaders(joinPoint, rabbitProducer, result);
            
            // 发送消息
            sendMessage(exchange, routingKey, message, headers, rabbitProducer);
            
        } catch (Exception e) {
            handleSendFailure(e, rabbitProducer, joinPoint);
        }
        
        return result;
    }

    /**
     * 检查是否应该发送消息
     */
    private boolean shouldSendMessage(ProceedingJoinPoint joinPoint, RabbitProducer rabbitProducer, Object result) {
        if (!StringUtils.hasText(rabbitProducer.condition())) {
            return true;
        }
        
        try {
            EvaluationContext context = createEvaluationContext(joinPoint, result);
            Expression expression = parser.parseExpression(rabbitProducer.condition());
            Boolean shouldSend = expression.getValue(context, Boolean.class);
            return shouldSend != null && shouldSend;
        } catch (Exception e) {
            log.warn("解析发送条件失败，默认发送消息: condition={}", rabbitProducer.condition(), e);
            return true;
        }
    }

    /**
     * 获取消息内容
     */
    private Object getMessageContent(ProceedingJoinPoint joinPoint, RabbitProducer rabbitProducer, Object result) {
        if (StringUtils.hasText(rabbitProducer.message())) {
            // 使用自定义消息表达式
            return parseExpression(rabbitProducer.message(), joinPoint, result);
        } else {
            // 使用方法返回值作为消息内容
            return result;
        }
    }

    /**
     * 构建消息头
     */
    private Map<String, Object> buildHeaders(ProceedingJoinPoint joinPoint, RabbitProducer rabbitProducer, Object result) {
        Map<String, Object> headers = new HashMap<>();
        
        // 添加消息类型
        if (StringUtils.hasText(rabbitProducer.messageType())) {
            headers.put(RabbitMQConstant.HEADER_MESSAGE_TYPE, rabbitProducer.messageType());
        }
        
        // 添加自定义头信息
        for (String header : rabbitProducer.headers()) {
            if (StringUtils.hasText(header) && header.contains("=")) {
                String[] parts = header.split("=", 2);
                String key = parts[0].trim();
                String value = parseExpression(parts[1].trim(), joinPoint, result);
                headers.put(key, value);
            }
        }
        
        // 添加方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        headers.put(RabbitMQConstant.HEADER_SOURCE_SERVICE, signature.getDeclaringType().getSimpleName());
        headers.put("methodName", signature.getName());
        
        return headers;
    }

    /**
     * 发送消息
     */
    private void sendMessage(String exchange, String routingKey, Object message, Map<String, Object> headers, RabbitProducer rabbitProducer) {
        // 设置默认值
        if (!StringUtils.hasText(exchange)) {
            exchange = RabbitMQConstant.DEFAULT_DIRECT_EXCHANGE;
        }
        if (!StringUtils.hasText(routingKey)) {
            routingKey = RabbitMQConstant.DEFAULT_ROUTING_KEY;
        }
        
        // 添加消息属性到头信息
        if (rabbitProducer.ttl() > 0) {
            headers.put("x-message-ttl", rabbitProducer.ttl());
        }
        if (rabbitProducer.priority() > 0) {
            headers.put("priority", rabbitProducer.priority());
        }
        
        try {
            if (rabbitProducer.async()) {
                // 异步发送
                CompletableFuture<Void> future = rabbitMQService.sendAsync(exchange, routingKey, message, headers);
                if (rabbitProducer.waitForConfirm()) {
                    future.get(); // 等待异步完成
                }
            } else if (rabbitProducer.waitForConfirm()) {
                // 同步发送并等待确认
                boolean confirmed = rabbitMQService.sendAndWaitForConfirm(exchange, routingKey, message, headers, rabbitProducer.confirmTimeout());
                if (!confirmed) {
                    throw new RuntimeException("消息发送确认超时");
                }
            } else {
                // 普通同步发送
                rabbitMQService.send(exchange, routingKey, message, headers);
            }
            
            log.debug("消息发送成功: exchange={}, routingKey={}, messageType={}", 
                    exchange, routingKey, rabbitProducer.messageType());
                    
        } catch (Exception e) {
            // 重试机制
            if (rabbitProducer.retryCount() > 0) {
                retryMessage(exchange, routingKey, message, headers, rabbitProducer, 0, e);
            } else {
                throw new RuntimeException("消息发送失败", e);
            }
        }
    }

    /**
     * 重试发送消息
     */
    private void retryMessage(String exchange, String routingKey, Object message, Map<String, Object> headers, 
                             RabbitProducer rabbitProducer, int currentRetry, Exception lastException) {
        if (currentRetry >= rabbitProducer.retryCount()) {
            throw new RuntimeException("消息发送重试失败，已达到最大重试次数: " + rabbitProducer.retryCount(), lastException);
        }
        
        try {
            // 等待重试间隔
            if (rabbitProducer.retryInterval() > 0) {
                Thread.sleep(rabbitProducer.retryInterval());
            }
            
            // 添加重试次数到头信息
            headers.put(RabbitMQConstant.HEADER_RETRY_COUNT, currentRetry + 1);
            
            // 重新发送
            rabbitMQService.send(exchange, routingKey, message, headers);
            
            log.info("消息重试发送成功: exchange={}, routingKey={}, retryCount={}", 
                    exchange, routingKey, currentRetry + 1);
                    
        } catch (Exception e) {
            log.warn("消息重试发送失败: exchange={}, routingKey={}, retryCount={}", 
                    exchange, routingKey, currentRetry + 1, e);
            retryMessage(exchange, routingKey, message, headers, rabbitProducer, currentRetry + 1, e);
        }
    }

    /**
     * 处理发送失败
     */
    private void handleSendFailure(Exception e, RabbitProducer rabbitProducer, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        
        switch (rabbitProducer.failureStrategy()) {
            case THROW_EXCEPTION:
                log.error("消息发送失败，抛出异常: method={}", methodName, e);
                throw new RuntimeException("消息发送失败: " + methodName, e);
                
            case LOG_AND_IGNORE:
                log.error("消息发送失败，记录日志并忽略: method={}", methodName, e);
                break;
                
            case SEND_TO_DLX:
                log.error("消息发送失败，发送到死信队列: method={}", methodName, e);
                try {
                    Map<String, Object> dlxHeaders = new HashMap<>();
                    dlxHeaders.put("originalMethod", methodName);
                    dlxHeaders.put("failureReason", e.getMessage());
                    dlxHeaders.put("failureTime", System.currentTimeMillis());
                    
                    rabbitMQService.send(RabbitMQConstant.DEAD_LETTER_EXCHANGE, 
                            RabbitMQConstant.DEAD_LETTER_ROUTING_KEY, 
                            joinPoint.getArgs(), dlxHeaders);
                } catch (Exception dlxException) {
                    log.error("发送到死信队列也失败了: method={}", methodName, dlxException);
                }
                break;
                
            case CUSTOM:
                log.error("消息发送失败，需要自定义处理: method={}", methodName, e);
                // 这里可以扩展自定义处理逻辑
                break;
                
            default:
                log.error("消息发送失败，未知处理策略: method={}, strategy={}", 
                        methodName, rabbitProducer.failureStrategy(), e);
                break;
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
        
        // 添加方法返回值
        context.setVariable("result", result);
        context.setVariable("returnValue", result);
        
        // 添加方法信息
        context.setVariable("method", method);
        context.setVariable("methodName", method.getName());
        context.setVariable("className", method.getDeclaringClass().getSimpleName());
        
        // 添加当前时间
        context.setVariable("currentTime", System.currentTimeMillis());
        context.setVariable("timestamp", System.currentTimeMillis());
        
        return context;
    }
}