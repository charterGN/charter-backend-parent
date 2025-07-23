package ink.charter.website.common.rabbitmq.annotation;

import java.lang.annotation.*;

/**
 * RabbitMQ生产者注解
 * 用于标记方法自动发送消息到指定的交换机和路由键
 *
 * @author charter
 * @create 2025/07/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbitProducer {

    /**
     * 交换机名称
     * 支持SpEL表达式
     */
    String exchange() default "";

    /**
     * 路由键
     * 支持SpEL表达式
     */
    String routingKey() default "";

    /**
     * 消息类型
     * 用于消费者识别消息类型
     */
    String messageType() default "";

    /**
     * 是否持久化消息
     */
    boolean persistent() default true;

    /**
     * 消息TTL（毫秒）
     * 0表示不设置TTL
     */
    long ttl() default 0;

    /**
     * 消息优先级
     * 0-255，数值越大优先级越高
     */
    int priority() default 0;

    /**
     * 是否等待确认
     * true: 等待broker确认消息已接收
     * false: 不等待确认，提高性能但可能丢失消息
     */
    boolean waitForConfirm() default false;

    /**
     * 确认超时时间（毫秒）
     * 仅在waitForConfirm=true时有效
     */
    long confirmTimeout() default 5000;

    /**
     * 发送条件
     * 支持SpEL表达式，返回boolean
     * 为true时才发送消息
     */
    String condition() default "";

    /**
     * 消息内容表达式
     * 支持SpEL表达式，用于自定义消息内容
     * 默认使用方法返回值作为消息内容
     */
    String message() default "";

    /**
     * 自定义消息头
     * 格式：key1=value1,key2=value2
     * 支持SpEL表达式
     */
    String[] headers() default {};

    /**
     * 是否异步发送
     */
    boolean async() default false;

    /**
     * 失败重试次数
     */
    int retryCount() default 0;

    /**
     * 重试间隔（毫秒）
     */
    long retryInterval() default 1000;

    /**
     * 发送失败时的处理策略
     */
    FailureStrategy failureStrategy() default FailureStrategy.THROW_EXCEPTION;

    /**
     * 发送失败处理策略枚举
     */
    enum FailureStrategy {
        /**
         * 抛出异常
         */
        THROW_EXCEPTION,
        
        /**
         * 记录日志并忽略
         */
        LOG_AND_IGNORE,
        
        /**
         * 发送到死信队列
         */
        SEND_TO_DLX,
        
        /**
         * 自定义处理
         */
        CUSTOM
    }
}