package ink.charter.website.common.rabbitmq.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * RabbitMQ服务接口
 * 定义消息发送、接收和管理的核心方法
 *
 * @author charter
 * @create 2025/07/19
 */
public interface RabbitMQService {

    // =============================消息发送=============================

    /**
     * 发送消息到默认交换机
     *
     * @param routingKey 路由键
     * @param message    消息内容
     */
    void send(String routingKey, Object message);

    /**
     * 发送消息到指定交换机
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     */
    void send(String exchange, String routingKey, Object message);

    /**
     * 发送消息到指定交换机（带消息属性）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param properties 消息属性
     */
    void send(String exchange, String routingKey, Object message, MessageProperties properties);

    /**
     * 发送消息到指定交换机（带自定义头信息）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param headers    自定义头信息
     */
    void send(String exchange, String routingKey, Object message, Map<String, Object> headers);

    /**
     * 异步发送消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @return CompletableFuture
     */
    CompletableFuture<Void> sendAsync(String exchange, String routingKey, Object message);

    /**
     * 异步发送消息（带自定义头信息）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param headers    自定义头信息
     * @return CompletableFuture
     */
    CompletableFuture<Void> sendAsync(String exchange, String routingKey, Object message, Map<String, Object> headers);

    // =============================延时消息=============================

    /**
     * 发送延时消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param delayMs    延时时间（毫秒）
     */
    void sendDelayMessage(String exchange, String routingKey, Object message, long delayMs);

    /**
     * 发送延时消息（带自定义头信息）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param delayMs    延时时间（毫秒）
     * @param headers    自定义头信息
     */
    void sendDelayMessage(String exchange, String routingKey, Object message, long delayMs, Map<String, Object> headers);

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
    boolean sendAndWaitForConfirm(String exchange, String routingKey, Object message, long timeout);

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
    boolean sendAndWaitForConfirm(String exchange, String routingKey, Object message, Map<String, Object> headers, long timeout);

    // =============================批量操作=============================

    /**
     * 批量发送消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param messages   消息列表
     */
    void sendBatch(String exchange, String routingKey, Object... messages);

    /**
     * 批量发送消息（不同路由键）
     *
     * @param exchange 交换机名称
     * @param messages 消息映射（路由键 -> 消息内容）
     */
    void sendBatch(String exchange, Map<String, Object> messages);

    // =============================消息接收=============================

    /**
     * 从队列接收单条消息
     *
     * @param queueName 队列名称
     * @param timeout   超时时间（毫秒）
     * @return 消息对象，超时返回null
     */
    Message receive(String queueName, long timeout);

    /**
     * 从队列接收单条消息并转换为指定类型
     *
     * @param queueName 队列名称
     * @param timeout   超时时间（毫秒）
     * @param clazz     目标类型
     * @param <T>       泛型类型
     * @return 转换后的对象，超时返回null
     */
    <T> T receiveAndConvert(String queueName, long timeout, Class<T> clazz);

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
    String declareQueue(String queueName, boolean durable, boolean exclusive, boolean autoDelete);

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
    String declareQueue(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments);

    /**
     * 删除队列
     *
     * @param queueName 队列名称
     * @return 是否删除成功
     */
    boolean deleteQueue(String queueName);

    /**
     * 清空队列
     *
     * @param queueName 队列名称
     * @return 清空的消息数量
     */
    int purgeQueue(String queueName);

    /**
     * 获取队列消息数量
     *
     * @param queueName 队列名称
     * @return 消息数量
     */
    long getQueueMessageCount(String queueName);

    /**
     * 获取队列消费者数量
     *
     * @param queueName 队列名称
     * @return 消费者数量
     */
    int getQueueConsumerCount(String queueName);

    // =============================交换机管理=============================

    /**
     * 创建直连交换机
     *
     * @param exchangeName 交换机名称
     * @param durable      是否持久化
     * @param autoDelete   是否自动删除
     */
    void declareDirectExchange(String exchangeName, boolean durable, boolean autoDelete);

    /**
     * 创建主题交换机
     *
     * @param exchangeName 交换机名称
     * @param durable      是否持久化
     * @param autoDelete   是否自动删除
     */
    void declareTopicExchange(String exchangeName, boolean durable, boolean autoDelete);

    /**
     * 创建扇形交换机
     *
     * @param exchangeName 交换机名称
     * @param durable      是否持久化
     * @param autoDelete   是否自动删除
     */
    void declareFanoutExchange(String exchangeName, boolean durable, boolean autoDelete);

    /**
     * 删除交换机
     *
     * @param exchangeName 交换机名称
     * @return 是否删除成功
     */
    boolean deleteExchange(String exchangeName);

    // =============================绑定管理=============================

    /**
     * 绑定队列到交换机
     *
     * @param queueName    队列名称
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     */
    void bindQueue(String queueName, String exchangeName, String routingKey);

    /**
     * 绑定队列到交换机（带参数）
     *
     * @param queueName    队列名称
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     * @param arguments    绑定参数
     */
    void bindQueue(String queueName, String exchangeName, String routingKey, Map<String, Object> arguments);

    /**
     * 解绑队列
     *
     * @param queueName    队列名称
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     */
    void unbindQueue(String queueName, String exchangeName, String routingKey);

    // =============================连接管理=============================

    /**
     * 检查连接状态
     *
     * @return 是否连接正常
     */
    boolean isConnectionOpen();

    /**
     * 获取连接信息
     *
     * @return 连接信息字符串
     */
    String getConnectionInfo();
}