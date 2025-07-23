package ink.charter.website.common.rabbitmq.constant;

/**
 * RabbitMQ常量类
 * 定义交换机、队列、路由键等常量
 *
 * @author charter
 * @create 2025/07/19
 */
public class RabbitMQConstant {

    // =============================交换机=============================
    
    /**
     * 默认直连交换机
     */
    public static final String DEFAULT_DIRECT_EXCHANGE = "charter.direct.exchange";
    
    /**
     * 默认主题交换机
     */
    public static final String DEFAULT_TOPIC_EXCHANGE = "charter.topic.exchange";
    
    /**
     * 默认扇形交换机
     */
    public static final String DEFAULT_FANOUT_EXCHANGE = "charter.fanout.exchange";
    
    /**
     * 死信交换机
     */
    public static final String DEAD_LETTER_EXCHANGE = "charter.dlx.exchange";
    
    /**
     * 延时交换机
     */
    public static final String DELAY_EXCHANGE = "charter.delay.exchange";

    // =============================队列=============================
    
    /**
     * 默认队列
     */
    public static final String DEFAULT_QUEUE = "charter.default.queue";
    
    /**
     * 死信队列
     */
    public static final String DEAD_LETTER_QUEUE = "charter.dlx.queue";
    
    /**
     * 延时队列
     */
    public static final String DELAY_QUEUE = "charter.delay.queue";
    
    /**
     * 用户相关队列
     */
    public static final String USER_QUEUE = "charter.user.queue";
    
    /**
     * 订单相关队列
     */
    public static final String ORDER_QUEUE = "charter.order.queue";
    
    /**
     * 邮件队列
     */
    public static final String EMAIL_QUEUE = "charter.email.queue";
    
    /**
     * 短信队列
     */
    public static final String SMS_QUEUE = "charter.sms.queue";

    // =============================路由键=============================
    
    /**
     * 默认路由键
     */
    public static final String DEFAULT_ROUTING_KEY = "charter.default";
    
    /**
     * 死信路由键
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "dlx";
    
    /**
     * 用户相关路由键
     */
    public static final String USER_ROUTING_KEY = "charter.user";
    
    /**
     * 订单相关路由键
     */
    public static final String ORDER_ROUTING_KEY = "charter.order";
    
    /**
     * 邮件路由键
     */
    public static final String EMAIL_ROUTING_KEY = "charter.email";
    
    /**
     * 短信路由键
     */
    public static final String SMS_ROUTING_KEY = "charter.sms";
    
    /**
     * 延时路由键
     */
    public static final String DELAY_ROUTING_KEY = "charter.delay";

    // =============================消息属性=============================
    
    /**
     * 消息TTL（毫秒）
     */
    public static final String MESSAGE_TTL = "x-message-ttl";
    
    /**
     * 队列TTL（毫秒）
     */
    public static final String QUEUE_TTL = "x-expires";
    
    /**
     * 队列最大长度
     */
    public static final String MAX_LENGTH = "x-max-length";
    
    /**
     * 队列最大长度字节数
     */
    public static final String MAX_LENGTH_BYTES = "x-max-length-bytes";
    
    /**
     * 死信交换机参数
     */
    public static final String DEAD_LETTER_EXCHANGE_PARAM = "x-dead-letter-exchange";
    
    /**
     * 死信路由键参数
     */
    public static final String DEAD_LETTER_ROUTING_KEY_PARAM = "x-dead-letter-routing-key";
    
    /**
     * 延时消息插件参数
     */
    public static final String DELAYED_TYPE = "x-delayed-type";

    // =============================默认配置=============================
    
    /**
     * 默认消息TTL（30分钟）
     */
    public static final long DEFAULT_MESSAGE_TTL = 30 * 60 * 1000L;
    
    /**
     * 默认队列最大长度
     */
    public static final int DEFAULT_MAX_LENGTH = 10000;
    
    /**
     * 默认重试次数
     */
    public static final int DEFAULT_RETRY_COUNT = 3;
    
    /**
     * 默认延时时间（秒）
     */
    public static final int DEFAULT_DELAY_SECONDS = 60;

    // =============================消息头=============================
    
    /**
     * 消息ID头
     */
    public static final String HEADER_MESSAGE_ID = "messageId";
    
    /**
     * 消息类型头
     */
    public static final String HEADER_MESSAGE_TYPE = "messageType";
    
    /**
     * 重试次数头
     */
    public static final String HEADER_RETRY_COUNT = "retryCount";
    
    /**
     * 创建时间头
     */
    public static final String HEADER_CREATE_TIME = "createTime";
    
    /**
     * 来源服务头
     */
    public static final String HEADER_SOURCE_SERVICE = "sourceService";
    
    /**
     * 目标服务头
     */
    public static final String HEADER_TARGET_SERVICE = "targetService";
    
    /**
     * 用户ID头
     */
    public static final String HEADER_USER_ID = "userId";
    
    /**
     * 租户ID头
     */
    public static final String HEADER_TENANT_ID = "tenantId";
}