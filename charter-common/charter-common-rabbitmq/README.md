# Charter RabbitMQ 中间件

基于Spring Boot的RabbitMQ消息队列中间件，提供简单易用的消息发送和消费功能。

## 功能特性

- 🚀 **注解驱动**: 使用`@RabbitProducer`和`@RabbitConsumer`注解简化消息发送和消费
- 🔧 **工具类支持**: 提供`RabbitMQUtils`工具类，支持各种消息操作
- ⚡ **异步支持**: 支持同步和异步消息发送
- ⏰ **延时消息**: 支持延时消息发送
- 🔄 **重试机制**: 内置重试机制和失败处理策略
- 💀 **死信队列**: 自动配置死信队列处理失败消息
- 📊 **批量操作**: 支持批量消息发送
- 🎯 **多种交换机**: 支持Direct、Topic、Fanout交换机
- 🔍 **SpEL表达式**: 支持Spring表达式语言进行条件判断
- ⚙️ **外部化配置**: 支持通过配置文件自定义各种参数

## 快速开始

### 1. 添加依赖

在你的项目中添加`common-rabbitmq`依赖：

```xml
<dependency>
    <groupId>ink.charter</groupId>
    <artifactId>common-rabbitmq</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置RabbitMQ

在`application.yml`中配置RabbitMQ连接信息：

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
    publisher-confirm-type: correlated
    publisher-returns: true
    listener:
      simple:
        acknowledge-mode: manual
        prefetch: 10
        concurrency: 2
        max-concurrency: 5

# Charter RabbitMQ 配置
charter:
  rabbitmq:
    enabled: true
    producer:
      enabled: true
      confirm-enabled: true
      return-enabled: true
      confirm-timeout: 5000
    consumer:
      enabled: true
      default-concurrency: "1-3"
      default-prefetch-count: 10
    retry:
      enabled: true
      max-attempts: 3
      initial-interval: 1000
      max-interval: 10000
      multiplier: 2.0
    dead-letter:
      enabled: true
      exchange: "dlx.exchange"
      queue: "dlx.queue"
      routing-key: "dlx"
```

## 使用方式

### 注解方式

#### 发送消息

```java
@Service
public class UserService {
    
    // 简单发送消息
    @RabbitProducer(
        exchange = "user.exchange",
        routingKey = "user.register",
        messageType = "USER_REGISTER"
    )
    public void sendUserRegisterMessage(String userId, String email) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("email", email);
        // 方法执行后自动发送消息
    }
    
    // 条件发送消息
    @RabbitProducer(
        exchange = "order.exchange",
        routingKey = "order.status",
        condition = "#status == 'PAID'", // 只有状态为PAID时才发送
        persistent = true,
        confirmation = true
    )
    public void updateOrderStatus(String orderId, String status) {
        // 业务逻辑
    }
    
    // 异步发送消息
    @RabbitProducer(
        exchange = "email.exchange",
        routingKey = "email.send",
        async = true,
        ttl = 300000 // 5分钟TTL
    )
    public void sendEmail(String email, String content) {
        // 异步发送邮件消息
    }
}
```

#### 消费消息

```java
@Component
public class MessageConsumer {
    
    // 简单消费消息
    @RabbitConsumer(
        queue = "user.queue",
        exchange = "user.exchange",
        routingKey = "user.register",
        exchangeType = ExchangeTypes.DIRECT
    )
    public void handleUserRegister(Map<String, Object> userInfo) {
        String userId = (String) userInfo.get("userId");
        String email = (String) userInfo.get("email");
        // 处理用户注册逻辑
    }
    
    // 主题模式消费
    @RabbitConsumer(
        queue = "order.queue",
        exchange = "order.exchange",
        routingKey = "order.*", // 匹配所有order开头的路由键
        exchangeType = ExchangeTypes.TOPIC,
        concurrency = "2-5",
        prefetchCount = 20,
        enableDlq = true
    )
    public void handleOrderMessage(Map<String, Object> orderInfo) {
        // 处理订单消息
    }
    
    // 条件消费
    @RabbitConsumer(
        queue = "email.queue",
        exchange = "email.exchange",
        routingKey = "email.send",
        condition = "#message.priority > 5", // 只处理优先级大于5的消息
        messageTypeFilter = "EMAIL_SEND"
    )
    public void handleHighPriorityEmail(Map<String, Object> emailInfo) {
        // 处理高优先级邮件
    }
}
```

### 工具类方式

```java
@Service
public class MessageService {
    
    public void sendMessages() {
        // 1. 简单发送消息
        RabbitMQUtils.sendToQueue("test.queue", "Hello RabbitMQ!");
        
        // 2. 发送对象消息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", "12345");
        userInfo.put("name", "张三");
        RabbitMQUtils.send("user.exchange", "user.register", userInfo);
        
        // 3. 发送带头信息的消息
        Map<String, Object> headers = new HashMap<>();
        headers.put("source", "user-web");
        headers.put("version", "1.0");
        RabbitMQUtils.send("user.exchange", "user.register", userInfo, headers);
        
        // 4. 异步发送消息
        RabbitMQUtils.sendAsync("order.exchange", "order.created", userInfo)
                .thenRun(() -> log.info("异步消息发送完成"))
                .exceptionally(throwable -> {
                    log.error("异步消息发送失败", throwable);
                    return null;
                });
        
        // 5. 发送延时消息
        RabbitMQUtils.sendDelayMessage("email.exchange", "email.send", 
                userInfo, 60000); // 1分钟后发送
        
        // 6. 批量发送消息
        RabbitMQUtils.sendBatch("broadcast.exchange", "news", 
                "消息1", "消息2", "消息3");
        
        // 7. 主题消息
        RabbitMQUtils.sendToTopic("topic.exchange", "user.register", userInfo);
        
        // 8. 广播消息
        RabbitMQUtils.broadcast("fanout.exchange", "系统维护通知");
        
        // 9. 发送并等待确认
        boolean success = RabbitMQUtils.sendAndWaitForConfirm(
                "order.exchange", "order.created", userInfo, 5000);
    }
    
    public void manageQueues() {
        // 创建队列
        String queueName = RabbitMQUtils.declareQueue("test.queue", true, false, false);
        
        // 创建交换机
        RabbitMQUtils.declareDirectExchange("test.exchange", true, false);
        RabbitMQUtils.declareTopicExchange("topic.exchange", true, false);
        RabbitMQUtils.declareFanoutExchange("fanout.exchange", true, false);
        
        // 绑定队列到交换机
        RabbitMQUtils.bindQueue("test.queue", "test.exchange", "test.routing.key");
        
        // 获取队列信息
        long messageCount = RabbitMQUtils.getQueueMessageCount("test.queue");
        int consumerCount = RabbitMQUtils.getQueueConsumerCount("test.queue");
        
        // 清空队列
        int purgedCount = RabbitMQUtils.purgeQueue("test.queue");
        
        // 检查连接状态
        boolean isConnected = RabbitMQUtils.isConnectionOpen();
    }
}
```

### 传统监听器方式

```java
@Component
public class TraditionalConsumer {
    
    @RabbitListener(queues = "sms.queue")
    public void handleSmsMessage(Map<String, Object> smsInfo) {
        String phone = (String) smsInfo.get("phone");
        String content = (String) smsInfo.get("content");
        // 处理短信发送
    }
}
```

## 常量定义

中间件提供了常用的常量定义，位于`RabbitMQConstant`类中：

```java
// 交换机
RabbitMQConstant.EXCHANGE_DIRECT
RabbitMQConstant.EXCHANGE_TOPIC
RabbitMQConstant.EXCHANGE_FANOUT
RabbitMQConstant.EXCHANGE_DLX
RabbitMQConstant.EXCHANGE_DELAY

// 队列
RabbitMQConstant.QUEUE_DEFAULT
RabbitMQConstant.QUEUE_USER
RabbitMQConstant.QUEUE_ORDER
RabbitMQConstant.QUEUE_EMAIL
RabbitMQConstant.QUEUE_SMS

// 路由键
RabbitMQConstant.ROUTING_KEY_DEFAULT
RabbitMQConstant.ROUTING_KEY_USER
RabbitMQConstant.ROUTING_KEY_ORDER
RabbitMQConstant.ROUTING_KEY_EMAIL
RabbitMQConstant.ROUTING_KEY_SMS
```

## 失败处理策略

### 生产者失败策略

- `THROW_EXCEPTION`: 抛出异常（默认）
- `LOG_AND_IGNORE`: 记录日志并忽略
- `SEND_TO_DLX`: 发送到死信队列
- `CUSTOM`: 自定义处理

### 消费者失败策略

- `RETRY_AND_DLX`: 重试后发送到死信队列（默认）
- `DIRECT_DLX`: 直接发送到死信队列
- `DISCARD`: 丢弃消息
- `REQUEUE`: 重新入队
- `CUSTOM`: 自定义处理

## 最佳实践

1. **合理设置并发数**: 根据业务需求和服务器性能设置合适的并发数
2. **使用死信队列**: 为重要消息启用死信队列，避免消息丢失
3. **设置合理的TTL**: 为消息设置合适的生存时间，避免消息堆积
4. **监控队列状态**: 定期检查队列消息数量和消费者数量
5. **异常处理**: 在消费者中正确处理异常，避免消息重复处理
6. **幂等性**: 确保消息处理的幂等性，避免重复处理带来的问题

## 注意事项

1. 确保RabbitMQ服务正常运行
2. 正确配置连接参数
3. 注意消息序列化和反序列化
4. 合理设置重试次数和间隔
5. 监控死信队列中的消息

## 依赖版本

- Spring Boot: 3.5.3
- Spring AMQP: 与Spring Boot版本对应
- FastJSON2: 2.0.57
- Hutool: 最新版本

## 更新日志

### v1.0.0
- 初始版本发布
- 支持注解驱动的消息发送和消费
- 提供工具类支持
- 支持延时消息、批量操作、死信队列等功能