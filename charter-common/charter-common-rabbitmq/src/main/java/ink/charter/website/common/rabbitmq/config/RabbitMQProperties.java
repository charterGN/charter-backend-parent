package ink.charter.website.common.rabbitmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ配置属性
 * 用于外部化配置RabbitMQ相关参数
 *
 * @author charter
 * @create 2025/07/19
 */
@Data
@Component
@ConfigurationProperties(prefix = "charter.rabbitmq")
public class RabbitMQProperties {

    /**
     * 是否启用RabbitMQ
     */
    private boolean enabled = true;

    /**
     * 生产者配置
     */
    private Producer producer = new Producer();

    /**
     * 消费者配置
     */
    private Consumer consumer = new Consumer();

    /**
     * 重试配置
     */
    private Retry retry = new Retry();

    /**
     * 死信队列配置
     */
    private DeadLetter deadLetter = new DeadLetter();

    /**
     * 延时消息配置
     */
    private Delay delay = new Delay();

    /**
     * 生产者配置
     */
    @Data
    public static class Producer {
        /**
         * 是否启用生产者切面
         */
        private boolean enabled = true;

        /**
         * 是否启用发布确认
         */
        private boolean confirmEnabled = true;

        /**
         * 是否启用发布返回
         */
        private boolean returnEnabled = true;

        /**
         * 确认超时时间（毫秒）
         */
        private long confirmTimeout = 5000;

        /**
         * 默认交换机
         */
        private String defaultExchange = "charter.exchange";

        /**
         * 默认路由键
         */
        private String defaultRoutingKey = "charter.routing.key";

        /**
         * 是否默认持久化消息
         */
        private boolean defaultPersistent = true;

        /**
         * 默认消息TTL（毫秒）
         */
        private Long defaultTtl;

        /**
         * 默认消息优先级
         */
        private Integer defaultPriority;
    }

    /**
     * 消费者配置
     */
    @Data
    public static class Consumer {
        /**
         * 是否启用消费者切面
         */
        private boolean enabled = true;

        /**
         * 默认并发数
         */
        private String defaultConcurrency = "1-3";

        /**
         * 默认预取数量
         */
        private int defaultPrefetchCount = 10;

        /**
         * 默认确认模式
         */
        private String defaultAckMode = "MANUAL";

        /**
         * 是否默认启用死信队列
         */
        private boolean defaultEnableDlq = true;

        /**
         * 默认队列持久化
         */
        private boolean defaultDurable = true;

        /**
         * 默认队列排他性
         */
        private boolean defaultExclusive = false;

        /**
         * 默认队列自动删除
         */
        private boolean defaultAutoDelete = false;
    }

    /**
     * 重试配置
     */
    @Data
    public static class Retry {
        /**
         * 是否启用重试
         */
        private boolean enabled = true;

        /**
         * 最大重试次数
         */
        private int maxAttempts = 3;

        /**
         * 初始重试间隔（毫秒）
         */
        private long initialInterval = 1000;

        /**
         * 最大重试间隔（毫秒）
         */
        private long maxInterval = 10000;

        /**
         * 重试间隔倍数
         */
        private double multiplier = 2.0;

        /**
         * 是否启用随机化
         */
        private boolean randomize = true;
    }

    /**
     * 死信队列配置
     */
    @Data
    public static class DeadLetter {
        /**
         * 是否启用死信队列
         */
        private boolean enabled = true;

        /**
         * 死信交换机名称
         */
        private String exchange = "dlx.exchange";

        /**
         * 死信队列名称
         */
        private String queue = "dlx.queue";

        /**
         * 死信路由键
         */
        private String routingKey = "dlx";

        /**
         * 死信队列TTL（毫秒）
         */
        private Long ttl = 86400000L; // 24小时

        /**
         * 死信队列最大长度
         */
        private Integer maxLength = 10000;
    }

    /**
     * 延时消息配置
     */
    @Data
    public static class Delay {
        /**
         * 是否启用延时消息
         */
        private boolean enabled = true;

        /**
         * 延时交换机名称
         */
        private String exchange = "delay.exchange";

        /**
         * 延时队列名称
         */
        private String queue = "delay.queue";

        /**
         * 延时路由键
         */
        private String routingKey = "delay";

        /**
         * 最大延时时间（毫秒）
         */
        private long maxDelayTime = 86400000L; // 24小时

        /**
         * 默认延时时间（毫秒）
         */
        private long defaultDelayTime = 60000L; // 1分钟
    }
}