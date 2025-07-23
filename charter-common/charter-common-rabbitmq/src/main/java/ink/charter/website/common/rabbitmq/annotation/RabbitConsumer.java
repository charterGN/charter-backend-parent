package ink.charter.website.common.rabbitmq.annotation;

import java.lang.annotation.*;

/**
 * RabbitMQ消费者注解
 * 用于标记方法自动消费指定队列的消息
 *
 * @author charter
 * @create 2025/07/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbitConsumer {

    /**
     * 队列名称
     * 支持SpEL表达式
     */
    String queue();

    /**
     * 交换机名称
     * 如果指定，会自动创建队列和绑定关系
     */
    String exchange() default "";

    /**
     * 路由键
     * 用于绑定队列到交换机
     * 支持通配符（*和#）
     */
    String routingKey() default "";

    /**
     * 交换机类型
     */
    ExchangeType exchangeType() default ExchangeType.DIRECT;

    /**
     * 是否持久化队列
     */
    boolean durable() default true;

    /**
     * 是否排他队列
     * 排他队列只能被声明它的连接使用
     */
    boolean exclusive() default false;

    /**
     * 是否自动删除队列
     * 当最后一个消费者断开连接时自动删除
     */
    boolean autoDelete() default false;

    /**
     * 并发消费者数量
     */
    int concurrency() default 1;

    /**
     * 最大并发消费者数量
     */
    int maxConcurrency() default 1;

    /**
     * 预取数量
     * 消费者一次性从队列中获取的消息数量
     */
    int prefetch() default 1;

    /**
     * 确认模式
     */
    AckMode ackMode() default AckMode.MANUAL;

    /**
     * 消息类型过滤
     * 只消费指定类型的消息
     */
    String messageType() default "";

    /**
     * 消费条件
     * 支持SpEL表达式，返回boolean
     * 为true时才消费消息
     */
    String condition() default "";

    /**
     * 重试次数
     */
    int retryCount() default 3;

    /**
     * 重试间隔（毫秒）
     */
    long retryInterval() default 1000;

    /**
     * 消费失败时的处理策略
     */
    FailureStrategy failureStrategy() default FailureStrategy.RETRY_AND_DLX;

    /**
     * 队列参数
     * 格式：key1=value1,key2=value2
     */
    String[] queueArguments() default {};

    /**
     * 是否启用死信队列
     */
    boolean enableDlx() default true;

    /**
     * 死信交换机
     */
    String dlxExchange() default "charter.dlx.exchange";

    /**
     * 死信路由键
     */
    String dlxRoutingKey() default "dlx";

    /**
     * 交换机类型枚举
     */
    enum ExchangeType {
        DIRECT, TOPIC, FANOUT, HEADERS
    }

    /**
     * 确认模式枚举
     */
    enum AckMode {
        /**
         * 自动确认
         */
        AUTO,
        
        /**
         * 手动确认
         */
        MANUAL,
        
        /**
         * 无确认
         */
        NONE
    }

    /**
     * 消费失败处理策略枚举
     */
    enum FailureStrategy {
        /**
         * 重试后发送到死信队列
         */
        RETRY_AND_DLX,
        
        /**
         * 直接发送到死信队列
         */
        DIRECT_DLX,
        
        /**
         * 丢弃消息
         */
        DISCARD,
        
        /**
         * 重新入队
         */
        REQUEUE,
        
        /**
         * 自定义处理
         */
        CUSTOM
    }
}