package ink.charter.website.common.rabbitmq.example;

import ink.charter.website.common.rabbitmq.annotation.RabbitConsumer;
import ink.charter.website.common.rabbitmq.annotation.RabbitProducer;
import ink.charter.website.common.rabbitmq.constant.RabbitMQConstant;
import ink.charter.website.common.rabbitmq.utils.RabbitMQUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ使用示例
 * 展示如何使用RabbitMQ中间件的各种功能
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
//@Component
public class RabbitMQExample {

    // =============================注解方式发送消息=============================

    /**
     * 使用注解发送用户注册消息
     */
    @RabbitProducer(
            exchange = RabbitMQConstant.DEFAULT_DIRECT_EXCHANGE,
            routingKey = RabbitMQConstant.USER_ROUTING_KEY,
            messageType = "USER_REGISTER",
            persistent = true,
            waitForConfirm = true
    )
    public void sendUserRegisterMessage(String userId, String email) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("email", email);
        userInfo.put("action", "register");
        log.info("发送用户注册消息: {}", userInfo);
    }

    /**
     * 使用注解发送延时消息
     */
    @RabbitProducer(
            exchange = RabbitMQConstant.DELAY_EXCHANGE,
            routingKey = RabbitMQConstant.EMAIL_ROUTING_KEY,
            messageType = "EMAIL_DELAY",
            ttl = 300000, // 5分钟TTL
            async = true
    )
    public void sendDelayEmailMessage(String email, String content) {
        Map<String, Object> emailInfo = new HashMap<>();
        emailInfo.put("email", email);
        emailInfo.put("content", content);
        emailInfo.put("type", "welcome");
        log.info("发送延时邮件消息: {}", emailInfo);
    }

    /**
     * 使用注解发送条件消息
     */
    @RabbitProducer(
            exchange = RabbitMQConstant.DEFAULT_TOPIC_EXCHANGE,
            routingKey = RabbitMQConstant.ORDER_ROUTING_KEY,
            messageType = "ORDER_STATUS",
            condition = "#status == 'PAID'", // 只有状态为PAID时才发送
            persistent = true
    )
    public void sendOrderStatusMessage(String orderId, String status, Double amount) {
        Map<String, Object> orderInfo = new HashMap<>();
        orderInfo.put("orderId", orderId);
        orderInfo.put("status", status);
        orderInfo.put("amount", amount);
        log.info("发送订单状态消息: {}", orderInfo);
    }

    // =============================注解方式消费消息=============================

    /**
     * 消费用户消息
     */
    @RabbitConsumer(
            queue = RabbitMQConstant.USER_QUEUE,
            exchange = RabbitMQConstant.DEFAULT_DIRECT_EXCHANGE,
            routingKey = RabbitMQConstant.USER_ROUTING_KEY,
            exchangeType = RabbitConsumer.ExchangeType.DIRECT,
            concurrency = 2,
            maxConcurrency = 5,
            prefetch = 10
    )
    public void handleUserMessage(Map<String, Object> userInfo) {
        log.info("处理用户消息: {}", userInfo);
        
        String action = (String) userInfo.get("action");
        String userId = (String) userInfo.get("userId");
        
        switch (action) {
            case "register":
                handleUserRegister(userId, (String) userInfo.get("email"));
                break;
            case "login":
                handleUserLogin(userId);
                break;
            default:
                log.warn("未知的用户操作: {}", action);
        }
    }

    /**
     * 消费订单消息（支持主题模式）
     */
    @RabbitConsumer(
            queue = RabbitMQConstant.ORDER_QUEUE,
            exchange = RabbitMQConstant.DEFAULT_TOPIC_EXCHANGE,
            routingKey = "order.*", // 匹配所有order开头的路由键
            exchangeType = RabbitConsumer.ExchangeType.TOPIC,
            messageType = "ORDER_STATUS",
            condition = "#message.status != null", // 只处理有状态的消息
            enableDlx = true
    )
    public void handleOrderMessage(Map<String, Object> orderInfo) {
        log.info("处理订单消息: {}", orderInfo);
        
        String orderId = (String) orderInfo.get("orderId");
        String status = (String) orderInfo.get("status");
        
        try {
            switch (status) {
                case "PAID":
                    handleOrderPaid(orderId);
                    break;
                case "SHIPPED":
                    handleOrderShipped(orderId);
                    break;
                case "DELIVERED":
                    handleOrderDelivered(orderId);
                    break;
                default:
                    log.warn("未知的订单状态: {}", status);
            }
        } catch (Exception e) {
            log.error("处理订单消息失败: {}", orderId, e);
            throw e; // 重新抛出异常，触发重试机制
        }
    }

    /**
     * 消费邮件消息
     */
    @RabbitConsumer(
            queue = RabbitMQConstant.EMAIL_QUEUE,
            exchange = RabbitMQConstant.DEFAULT_DIRECT_EXCHANGE,
            routingKey = RabbitMQConstant.EMAIL_ROUTING_KEY,
            exchangeType = RabbitConsumer.ExchangeType.DIRECT,
            concurrency = 1,
            maxConcurrency = 3,
            prefetch = 5
    )
    public void handleEmailMessage(Map<String, Object> emailInfo) {
        log.info("处理邮件消息: {}", emailInfo);
        
        String email = (String) emailInfo.get("email");
        String content = (String) emailInfo.get("content");
        String type = (String) emailInfo.get("type");
        
        try {
            sendEmail(email, content, type);
            log.info("邮件发送成功: {}", email);
        } catch (Exception e) {
            log.error("邮件发送失败: {}", email, e);
            throw e;
        }
    }

    // =============================工具类方式使用=============================

    /**
     * 使用工具类发送消息示例
     */
    public void utilsExample() {
        // 1. 简单发送消息
        RabbitMQUtils.sendToQueue(RabbitMQConstant.DEFAULT_QUEUE, "Hello RabbitMQ!");
        
        // 2. 发送对象消息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", "12345");
        userInfo.put("name", "张三");
        RabbitMQUtils.send(RabbitMQConstant.DEFAULT_DIRECT_EXCHANGE, RabbitMQConstant.USER_ROUTING_KEY, userInfo);
        
        // 3. 发送带头信息的消息
        Map<String, Object> headers = new HashMap<>();
        headers.put("source", "user-web");
        headers.put("version", "1.0");
        RabbitMQUtils.send(RabbitMQConstant.DEFAULT_DIRECT_EXCHANGE, RabbitMQConstant.USER_ROUTING_KEY, userInfo, headers);
        
        // 4. 异步发送消息
        RabbitMQUtils.sendAsync(RabbitMQConstant.DEFAULT_TOPIC_EXCHANGE, "order.created", userInfo)
                .thenRun(() -> log.info("异步消息发送完成"))
                .exceptionally(throwable -> {
                    log.error("异步消息发送失败", throwable);
                    return null;
                });
        
        // 5. 发送延时消息
        RabbitMQUtils.sendDelayMessage(RabbitMQConstant.DELAY_EXCHANGE, RabbitMQConstant.EMAIL_ROUTING_KEY, 
                userInfo, 60000); // 1分钟后发送
        
        // 6. 批量发送消息
        RabbitMQUtils.sendBatch(RabbitMQConstant.DEFAULT_FANOUT_EXCHANGE, RabbitMQConstant.DEFAULT_ROUTING_KEY, 
                "消息1", "消息2", "消息3");
        
        // 7. 主题消息
        RabbitMQUtils.sendToTopic(RabbitMQConstant.DEFAULT_TOPIC_EXCHANGE, "user.register", userInfo);
        
        // 8. 广播消息
        RabbitMQUtils.broadcast(RabbitMQConstant.DEFAULT_FANOUT_EXCHANGE, "系统维护通知");
        
        // 9. 发送并等待确认
        boolean success = RabbitMQUtils.sendAndWaitForConfirm(
                RabbitMQConstant.DEFAULT_DIRECT_EXCHANGE, 
                RabbitMQConstant.ORDER_ROUTING_KEY, 
                userInfo, 
                5000 // 5秒超时
        );
        log.info("消息发送确认结果: {}", success);
    }

    /**
     * 队列管理示例
     */
    public void queueManagementExample() {
        // 1. 创建队列
        String queueName = RabbitMQUtils.declareQueue("test.queue", true, false, false);
        log.info("创建队列: {}", queueName);
        
        // 2. 创建带参数的队列
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 60000); // 消息TTL
        arguments.put("x-max-length", 1000);   // 最大长度
        RabbitMQUtils.declareQueue("test.queue.with.args", true, false, false, arguments);
        
        // 3. 创建交换机
        RabbitMQUtils.declareDirectExchange("test.exchange", true, false);
        RabbitMQUtils.declareTopicExchange("test.topic.exchange", true, false);
        RabbitMQUtils.declareFanoutExchange("test.fanout.exchange", true, false);
        
        // 4. 绑定队列到交换机
        RabbitMQUtils.bindQueue("test.queue", "test.exchange", "test.routing.key");
        
        // 5. 获取队列信息
        long messageCount = RabbitMQUtils.getQueueMessageCount("test.queue");
        int consumerCount = RabbitMQUtils.getQueueConsumerCount("test.queue");
        log.info("队列消息数: {}, 消费者数: {}", messageCount, consumerCount);
        
        // 6. 清空队列
        int purgedCount = RabbitMQUtils.purgeQueue("test.queue");
        log.info("清空队列，删除消息数: {}", purgedCount);
        
        // 7. 检查连接状态
        boolean isConnected = RabbitMQUtils.isConnectionOpen();
        log.info("连接状态: {}", isConnected);
        
        String connectionInfo = RabbitMQUtils.getConnectionInfo();
        log.info("连接信息: {}", connectionInfo);
    }

    // =============================传统监听器方式=============================

    /**
     * 使用传统@RabbitListener注解
     */
    @RabbitListener(queues = RabbitMQConstant.SMS_QUEUE)
    public void handleSmsMessage(Map<String, Object> smsInfo) {
        log.info("处理短信消息: {}", smsInfo);
        
        String phone = (String) smsInfo.get("phone");
        String content = (String) smsInfo.get("content");
        String type = (String) smsInfo.get("type");
        
        try {
            sendSms(phone, content, type);
            log.info("短信发送成功: {}", phone);
        } catch (Exception e) {
            log.error("短信发送失败: {}", phone, e);
            throw e;
        }
    }

    // =============================业务处理方法=============================

    private void handleUserRegister(String userId, String email) {
        log.info("处理用户注册: userId={}, email={}", userId, email);
        // 发送欢迎邮件
        Map<String, Object> emailInfo = new HashMap<>();
        emailInfo.put("email", email);
        emailInfo.put("content", "欢迎注册我们的网站！");
        emailInfo.put("type", "welcome");
        RabbitMQUtils.send(RabbitMQConstant.DEFAULT_DIRECT_EXCHANGE, RabbitMQConstant.EMAIL_ROUTING_KEY, emailInfo);
    }

    private void handleUserLogin(String userId) {
        log.info("处理用户登录: userId={}", userId);
        // 记录登录日志
    }

    private void handleOrderPaid(String orderId) {
        log.info("处理订单支付: orderId={}", orderId);
        // 更新订单状态，发送发货通知
    }

    private void handleOrderShipped(String orderId) {
        log.info("处理订单发货: orderId={}", orderId);
        // 发送物流信息
    }

    private void handleOrderDelivered(String orderId) {
        log.info("处理订单送达: orderId={}", orderId);
        // 发送评价邀请
    }

    private void sendEmail(String email, String content, String type) {
        log.info("发送邮件: email={}, type={}, content={}", email, type, content);
        // 实际的邮件发送逻辑
    }

    private void sendSms(String phone, String content, String type) {
        log.info("发送短信: phone={}, type={}, content={}", phone, type, content);
        // 实际的短信发送逻辑
    }
}