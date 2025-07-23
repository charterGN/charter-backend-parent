package ink.charter.website.common.rabbitmq.utils;

import ink.charter.website.common.rabbitmq.service.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * RabbitMQ工具类
 * 提供静态方法调用RabbitMQ服务
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@Component
public class RabbitMQUtils implements ApplicationContextAware {

    private static RabbitMQService rabbitMQService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        rabbitMQService = applicationContext.getBean(RabbitMQService.class);
    }

    // =============================消息发送=============================

    /**
     * 发送消息到默认交换机
     *
     * @param routingKey 路由键
     * @param message    消息内容
     */
    public static void send(String routingKey, Object message) {
        rabbitMQService.send(routingKey, message);
    }

    /**
     * 发送消息到指定交换机
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     */
    public static void send(String exchange, String routingKey, Object message) {
        rabbitMQService.send(exchange, routingKey, message);
    }

    /**
     * 发送消息到指定交换机（带自定义头信息）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param headers    自定义头信息
     */
    public static void send(String exchange, String routingKey, Object message, Map<String, Object> headers) {
        rabbitMQService.send(exchange, routingKey, message, headers);
    }

    /**
     * 异步发送消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @return CompletableFuture
     */
    public static CompletableFuture<Void> sendAsync(String exchange, String routingKey, Object message) {
        return rabbitMQService.sendAsync(exchange, routingKey, message);
    }

    /**
     * 异步发送消息（带自定义头信息）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param headers    自定义头信息
     * @return CompletableFuture
     */
    public static CompletableFuture<Void> sendAsync(String exchange, String routingKey, Object message, Map<String, Object> headers) {
        return rabbitMQService.sendAsync(exchange, routingKey, message, headers);
    }

    // =============================延时消息=============================

    /**
     * 发送延时消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param delayMs    延时时间（毫秒）
     */
    public static void sendDelayMessage(String exchange, String routingKey, Object message, long delayMs) {
        rabbitMQService.sendDelayMessage(exchange, routingKey, message, delayMs);
    }

    /**
     * 发送延时消息（带自定义头信息）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param delayMs    延时时间（毫秒）
     * @param headers    自定义头信息
     */
    public static void sendDelayMessage(String exchange, String routingKey, Object message, long delayMs, Map<String, Object> headers) {
        rabbitMQService.sendDelayMessage(exchange, routingKey, message, delayMs, headers);
    }

    // =============================消息确认=============================

    /**
     * 发送消息并等待确认
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param timeout    超时时间（毫秒）
     * @return 是否发送成功
     */
    public static boolean sendAndWaitForConfirm(String exchange, String routingKey, Object message, long timeout) {
        return rabbitMQService.sendAndWaitForConfirm(exchange, routingKey, message, timeout);
    }

    /**
     * 发送消息并等待确认（带自定义头信息）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param headers    自定义头信息
     * @param timeout    超时时间（毫秒）
     * @return 是否发送成功
     */
    public static boolean sendAndWaitForConfirm(String exchange, String routingKey, Object message, Map<String, Object> headers, long timeout) {
        return rabbitMQService.sendAndWaitForConfirm(exchange, routingKey, message, headers, timeout);
    }

    // =============================批量操作=============================

    /**
     * 批量发送消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param messages   消息列表
     */
    public static void sendBatch(String exchange, String routingKey, Object... messages) {
        rabbitMQService.sendBatch(exchange, routingKey, messages);
    }

    /**
     * 批量发送消息（不同路由键）
     *
     * @param exchange 交换机名称
     * @param messages 消息映射（路由键 -> 消息内容）
     */
    public static void sendBatch(String exchange, Map<String, Object> messages) {
        rabbitMQService.sendBatch(exchange, messages);
    }

    // =============================消息接收=============================

    /**
     * 从队列接收单条消息
     *
     * @param queueName 队列名称
     * @param timeout   超时时间（毫秒）
     * @return 消息对象，超时返回null
     */
    public static Message receive(String queueName, long timeout) {
        return rabbitMQService.receive(queueName, timeout);
    }

    /**
     * 从队列接收单条消息并转换为指定类型
     *
     * @param queueName 队列名称
     * @param timeout   超时时间（毫秒）
     * @param clazz     目标类型
     * @param <T>       泛型类型
     * @return 转换后的对象，超时返回null
     */
    public static <T> T receiveAndConvert(String queueName, long timeout, Class<T> clazz) {
        return rabbitMQService.receiveAndConvert(queueName, timeout, clazz);
    }

    // =============================队列管理=============================

    /**
     * 创建队列
     *
     * @param queueName 队列名称
     * @param durable   是否持久化
     * @param exclusive 是否排他
     * @param autoDelete 是否自动删除
     * @return 队列名称
     */
    public static String declareQueue(String queueName, boolean durable, boolean exclusive, boolean autoDelete) {
        return rabbitMQService.declareQueue(queueName, durable, exclusive, autoDelete);
    }

    /**
     * 创建队列（带参数）
     *
     * @param queueName 队列名称
     * @param durable   是否持久化
     * @param exclusive 是否排他
     * @param autoDelete 是否自动删除
     * @param arguments 队列参数
     * @return 队列名称
     */
    public static String declareQueue(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
        return rabbitMQService.declareQueue(queueName, durable, exclusive, autoDelete, arguments);
    }

    /**
     * 删除队列
     *
     * @param queueName 队列名称
     * @return 是否删除成功
     */
    public static boolean deleteQueue(String queueName) {
        return rabbitMQService.deleteQueue(queueName);
    }

    /**
     * 清空队列
     *
     * @param queueName 队列名称
     * @return 清空的消息数量
     */
    public static int purgeQueue(String queueName) {
        return rabbitMQService.purgeQueue(queueName);
    }

    /**
     * 获取队列消息数量
     *
     * @param queueName 队列名称
     * @return 消息数量
     */
    public static long getQueueMessageCount(String queueName) {
        return rabbitMQService.getQueueMessageCount(queueName);
    }

    /**
     * 获取队列消费者数量
     *
     * @param queueName 队列名称
     * @return 消费者数量
     */
    public static int getQueueConsumerCount(String queueName) {
        return rabbitMQService.getQueueConsumerCount(queueName);
    }

    // =============================交换机管理=============================

    /**
     * 创建直连交换机
     *
     * @param exchangeName 交换机名称
     * @param durable      是否持久化
     * @param autoDelete   是否自动删除
     */
    public static void declareDirectExchange(String exchangeName, boolean durable, boolean autoDelete) {
        rabbitMQService.declareDirectExchange(exchangeName, durable, autoDelete);
    }

    /**
     * 创建主题交换机
     *
     * @param exchangeName 交换机名称
     * @param durable      是否持久化
     * @param autoDelete   是否自动删除
     */
    public static void declareTopicExchange(String exchangeName, boolean durable, boolean autoDelete) {
        rabbitMQService.declareTopicExchange(exchangeName, durable, autoDelete);
    }

    /**
     * 创建扇形交换机
     *
     * @param exchangeName 交换机名称
     * @param durable      是否持久化
     * @param autoDelete   是否自动删除
     */
    public static void declareFanoutExchange(String exchangeName, boolean durable, boolean autoDelete) {
        rabbitMQService.declareFanoutExchange(exchangeName, durable, autoDelete);
    }

    /**
     * 删除交换机
     *
     * @param exchangeName 交换机名称
     * @return 是否删除成功
     */
    public static boolean deleteExchange(String exchangeName) {
        return rabbitMQService.deleteExchange(exchangeName);
    }

    // =============================绑定管理=============================

    /**
     * 绑定队列到交换机
     *
     * @param queueName    队列名称
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     */
    public static void bindQueue(String queueName, String exchangeName, String routingKey) {
        rabbitMQService.bindQueue(queueName, exchangeName, routingKey);
    }

    /**
     * 绑定队列到交换机（带参数）
     *
     * @param queueName    队列名称
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     * @param arguments    绑定参数
     */
    public static void bindQueue(String queueName, String exchangeName, String routingKey, Map<String, Object> arguments) {
        rabbitMQService.bindQueue(queueName, exchangeName, routingKey, arguments);
    }

    /**
     * 解绑队列
     *
     * @param queueName    队列名称
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     */
    public static void unbindQueue(String queueName, String exchangeName, String routingKey) {
        rabbitMQService.unbindQueue(queueName, exchangeName, routingKey);
    }

    // =============================连接管理=============================

    /**
     * 检查连接状态
     *
     * @return 是否连接正常
     */
    public static boolean isConnectionOpen() {
        return rabbitMQService.isConnectionOpen();
    }

    /**
     * 获取连接信息
     *
     * @return 连接信息字符串
     */
    public static String getConnectionInfo() {
        return rabbitMQService.getConnectionInfo();
    }

    // =============================便捷方法=============================

    /**
     * 快速发送文本消息
     *
     * @param queueName 队列名称
     * @param message   消息内容
     */
    public static void sendToQueue(String queueName, String message) {
        send("", queueName, message);
    }

    /**
     * 快速发送对象消息
     *
     * @param queueName 队列名称
     * @param message   消息对象
     */
    public static void sendToQueue(String queueName, Object message) {
        send("", queueName, message);
    }

    /**
     * 快速发送主题消息
     *
     * @param topicExchange 主题交换机
     * @param routingKey    路由键
     * @param message       消息内容
     */
    public static void sendToTopic(String topicExchange, String routingKey, Object message) {
        send(topicExchange, routingKey, message);
    }

    /**
     * 快速广播消息
     *
     * @param fanoutExchange 扇形交换机
     * @param message        消息内容
     */
    public static void broadcast(String fanoutExchange, Object message) {
        send(fanoutExchange, "", message);
    }

    /**
     * 检查服务是否可用
     *
     * @return 服务是否可用
     */
    public static boolean isServiceAvailable() {
        try {
            return rabbitMQService != null && isConnectionOpen();
        } catch (Exception e) {
            log.error("检查RabbitMQ服务可用性失败", e);
            return false;
        }
    }
}