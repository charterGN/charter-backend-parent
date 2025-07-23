package ink.charter.website.common.rabbitmq.service.impl;

import com.alibaba.fastjson2.JSON;
import ink.charter.website.common.rabbitmq.constant.RabbitMQConstant;
import ink.charter.website.common.rabbitmq.service.RabbitMQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ服务实现类
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQServiceImpl implements RabbitMQService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;
    private final ConnectionFactory connectionFactory;

    // =============================消息发送=============================

    @Override
    public void send(String routingKey, Object message) {
        send("", routingKey, message);
    }

    @Override
    public void send(String exchange, String routingKey, Object message) {
        try {
            MessageProperties properties = createDefaultMessageProperties();
            send(exchange, routingKey, message, properties);
        } catch (Exception e) {
            log.error("发送消息失败: exchange={}, routingKey={}, message={}", exchange, routingKey, message, e);
            throw new RuntimeException("消息发送失败", e);
        }
    }

    @Override
    public void send(String exchange, String routingKey, Object message, MessageProperties properties) {
        try {
            // 设置消息ID和时间戳
            if (!StringUtils.hasText(properties.getMessageId())) {
                properties.setMessageId(UUID.randomUUID().toString());
            }
            if (properties.getTimestamp() == null) {
                properties.setTimestamp(new java.util.Date());
            }
            
            // 创建消息对象
            Message msg = new Message(JSON.toJSONBytes(message), properties);
            
            // 发送消息
            rabbitTemplate.send(exchange, routingKey, msg);
            
            log.debug("消息发送成功: exchange={}, routingKey={}, messageId={}", 
                    exchange, routingKey, properties.getMessageId());
        } catch (Exception e) {
            log.error("发送消息失败: exchange={}, routingKey={}, message={}", exchange, routingKey, message, e);
            throw new RuntimeException("消息发送失败", e);
        }
    }

    @Override
    public void send(String exchange, String routingKey, Object message, Map<String, Object> headers) {
        try {
            MessageProperties properties = createDefaultMessageProperties();
            if (headers != null && !headers.isEmpty()) {
                properties.getHeaders().putAll(headers);
            }
            send(exchange, routingKey, message, properties);
        } catch (Exception e) {
            log.error("发送消息失败: exchange={}, routingKey={}, message={}, headers={}", 
                    exchange, routingKey, message, headers, e);
            throw new RuntimeException("消息发送失败", e);
        }
    }

    @Override
    @Async
    public CompletableFuture<Void> sendAsync(String exchange, String routingKey, Object message) {
        return CompletableFuture.runAsync(() -> send(exchange, routingKey, message));
    }

    @Override
    @Async
    public CompletableFuture<Void> sendAsync(String exchange, String routingKey, Object message, Map<String, Object> headers) {
        return CompletableFuture.runAsync(() -> send(exchange, routingKey, message, headers));
    }

    // =============================延时消息=============================

    @Override
    public void sendDelayMessage(String exchange, String routingKey, Object message, long delayMs) {
        sendDelayMessage(exchange, routingKey, message, delayMs, null);
    }

    @Override
    public void sendDelayMessage(String exchange, String routingKey, Object message, long delayMs, Map<String, Object> headers) {
        try {
            MessageProperties properties = createDefaultMessageProperties();
            
            // 设置延时
            properties.setDelayLong(delayMs);
            
            // 添加自定义头信息
            if (headers != null && !headers.isEmpty()) {
                properties.getHeaders().putAll(headers);
            }
            
            send(exchange, routingKey, message, properties);
            
            log.debug("延时消息发送成功: exchange={}, routingKey={}, delayMs={}", exchange, routingKey, delayMs);
        } catch (Exception e) {
            log.error("发送延时消息失败: exchange={}, routingKey={}, delayMs={}", exchange, routingKey, delayMs, e);
            throw new RuntimeException("延时消息发送失败", e);
        }
    }

    // =============================消息确认=============================

    @Override
    public boolean sendAndWaitForConfirm(String exchange, String routingKey, Object message, long timeout) {
        return sendAndWaitForConfirm(exchange, routingKey, message, null, timeout);
    }

    @Override
    public boolean sendAndWaitForConfirm(String exchange, String routingKey, Object message, Map<String, Object> headers, long timeout) {
        try {
            MessageProperties properties = createDefaultMessageProperties();
            if (headers != null && !headers.isEmpty()) {
                properties.getHeaders().putAll(headers);
            }
            
            Message msg = new Message(JSON.toJSONBytes(message), properties);
            rabbitTemplate.send(exchange, routingKey, msg);
            
            // 等待确认
            return rabbitTemplate.waitForConfirms(timeout);
        } catch (Exception e) {
            log.error("发送消息并等待确认失败: exchange={}, routingKey={}", exchange, routingKey, e);
            return false;
        }
    }

    // =============================批量操作=============================

    @Override
    public void sendBatch(String exchange, String routingKey, Object... messages) {
        try {
            for (Object message : messages) {
                send(exchange, routingKey, message);
            }
            log.debug("批量发送消息成功: exchange={}, routingKey={}, count={}", exchange, routingKey, messages.length);
        } catch (Exception e) {
            log.error("批量发送消息失败: exchange={}, routingKey={}", exchange, routingKey, e);
            throw new RuntimeException("批量消息发送失败", e);
        }
    }

    @Override
    public void sendBatch(String exchange, Map<String, Object> messages) {
        try {
            for (Map.Entry<String, Object> entry : messages.entrySet()) {
                send(exchange, entry.getKey(), entry.getValue());
            }
            log.debug("批量发送消息成功: exchange={}, count={}", exchange, messages.size());
        } catch (Exception e) {
            log.error("批量发送消息失败: exchange={}", exchange, e);
            throw new RuntimeException("批量消息发送失败", e);
        }
    }

    // =============================消息接收=============================

    @Override
    public Message receive(String queueName, long timeout) {
        try {
            return rabbitTemplate.receive(queueName, timeout);
        } catch (Exception e) {
            log.error("接收消息失败: queueName={}", queueName, e);
            throw new RuntimeException("消息接收失败", e);
        }
    }

    @Override
    public <T> T receiveAndConvert(String queueName, long timeout, Class<T> clazz) {
        try {
            Object result = rabbitTemplate.receiveAndConvert(queueName, timeout);
            if (result == null) {
                return null;
            }
            
            if (clazz.isInstance(result)) {
                return clazz.cast(result);
            }
            
            // 尝试JSON转换
            if (result instanceof String) {
                return JSON.parseObject((String) result, clazz);
            } else if (result instanceof byte[]) {
                return JSON.parseObject(new String((byte[]) result), clazz);
            }
            
            return JSON.parseObject(JSON.toJSONString(result), clazz);
        } catch (Exception e) {
            log.error("接收并转换消息失败: queueName={}, clazz={}", queueName, clazz.getName(), e);
            throw new RuntimeException("消息接收转换失败", e);
        }
    }

    // =============================队列管理=============================

    @Override
    public String declareQueue(String queueName, boolean durable, boolean exclusive, boolean autoDelete) {
        return declareQueue(queueName, durable, exclusive, autoDelete, null);
    }

    @Override
    public String declareQueue(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
        try {
            Queue queue = new Queue(queueName, durable, exclusive, autoDelete, arguments);
            return rabbitAdmin.declareQueue(queue);
        } catch (Exception e) {
            log.error("创建队列失败: queueName={}", queueName, e);
            throw new RuntimeException("队列创建失败", e);
        }
    }

    @Override
    public boolean deleteQueue(String queueName) {
        try {
            return rabbitAdmin.deleteQueue(queueName);
        } catch (Exception e) {
            log.error("删除队列失败: queueName={}", queueName, e);
            return false;
        }
    }

    @Override
    public int purgeQueue(String queueName) {
        try {
            return rabbitAdmin.purgeQueue(queueName);
        } catch (Exception e) {
            log.error("清空队列失败: queueName={}", queueName, e);
            throw new RuntimeException("队列清空失败", e);
        }
    }

    @Override
    public long getQueueMessageCount(String queueName) {
        try {
            Properties properties = rabbitAdmin.getQueueProperties(queueName);
            if (properties != null) {
                return (Integer) properties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);
            }
            return 0;
        } catch (Exception e) {
            log.error("获取队列消息数量失败: queueName={}", queueName, e);
            return 0;
        }
    }

    @Override
    public int getQueueConsumerCount(String queueName) {
        try {
            Properties properties = rabbitAdmin.getQueueProperties(queueName);
            if (properties != null) {
                return (Integer) properties.get(RabbitAdmin.QUEUE_CONSUMER_COUNT);
            }
            return 0;
        } catch (Exception e) {
            log.error("获取队列消费者数量失败: queueName={}", queueName, e);
            return 0;
        }
    }

    // =============================交换机管理=============================

    @Override
    public void declareDirectExchange(String exchangeName, boolean durable, boolean autoDelete) {
        try {
            DirectExchange exchange = new DirectExchange(exchangeName, durable, autoDelete);
            rabbitAdmin.declareExchange(exchange);
        } catch (Exception e) {
            log.error("创建直连交换机失败: exchangeName={}", exchangeName, e);
            throw new RuntimeException("交换机创建失败", e);
        }
    }

    @Override
    public void declareTopicExchange(String exchangeName, boolean durable, boolean autoDelete) {
        try {
            TopicExchange exchange = new TopicExchange(exchangeName, durable, autoDelete);
            rabbitAdmin.declareExchange(exchange);
        } catch (Exception e) {
            log.error("创建主题交换机失败: exchangeName={}", exchangeName, e);
            throw new RuntimeException("交换机创建失败", e);
        }
    }

    @Override
    public void declareFanoutExchange(String exchangeName, boolean durable, boolean autoDelete) {
        try {
            FanoutExchange exchange = new FanoutExchange(exchangeName, durable, autoDelete);
            rabbitAdmin.declareExchange(exchange);
        } catch (Exception e) {
            log.error("创建扇形交换机失败: exchangeName={}", exchangeName, e);
            throw new RuntimeException("交换机创建失败", e);
        }
    }

    @Override
    public boolean deleteExchange(String exchangeName) {
        try {
            return rabbitAdmin.deleteExchange(exchangeName);
        } catch (Exception e) {
            log.error("删除交换机失败: exchangeName={}", exchangeName, e);
            return false;
        }
    }

    // =============================绑定管理=============================

    @Override
    public void bindQueue(String queueName, String exchangeName, String routingKey) {
        bindQueue(queueName, exchangeName, routingKey, null);
    }

    @Override
    public void bindQueue(String queueName, String exchangeName, String routingKey, Map<String, Object> arguments) {
        try {
            Binding binding = BindingBuilder.bind(new Queue(queueName))
                    .to(new DirectExchange(exchangeName))
                    .with(routingKey);
            if (arguments != null && !arguments.isEmpty()) {
                binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, arguments);
            }
            rabbitAdmin.declareBinding(binding);
        } catch (Exception e) {
            log.error("绑定队列失败: queueName={}, exchangeName={}, routingKey={}", queueName, exchangeName, routingKey, e);
            throw new RuntimeException("队列绑定失败", e);
        }
    }

    @Override
    public void unbindQueue(String queueName, String exchangeName, String routingKey) {
        try {
            Binding binding = BindingBuilder.bind(new Queue(queueName))
                    .to(new DirectExchange(exchangeName))
                    .with(routingKey);
            rabbitAdmin.removeBinding(binding);
        } catch (Exception e) {
            log.error("解绑队列失败: queueName={}, exchangeName={}, routingKey={}", queueName, exchangeName, routingKey, e);
            throw new RuntimeException("队列解绑失败", e);
        }
    }

    // =============================连接管理=============================

    @Override
    public boolean isConnectionOpen() {
        try {
            Connection connection = connectionFactory.createConnection();
            boolean isOpen = connection.isOpen();
            connection.close();
            return isOpen;
        } catch (Exception e) {
            log.error("检查连接状态失败", e);
            return false;
        }
    }

    @Override
    public String getConnectionInfo() {
        try {
            Connection connection = connectionFactory.createConnection();
            String info = connection.toString();
            connection.close();
            return info;
        } catch (Exception e) {
            log.error("获取连接信息失败", e);
            return "连接信息获取失败: " + e.getMessage();
        }
    }

    // =============================私有方法=============================

    /**
     * 创建默认消息属性
     */
    private MessageProperties createDefaultMessageProperties() {
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setContentEncoding("UTF-8");
        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        properties.setMessageId(UUID.randomUUID().toString());
        properties.setTimestamp(new java.util.Date());
        
        // 添加默认头信息
        properties.setHeader(RabbitMQConstant.HEADER_CREATE_TIME, 
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return properties;
    }
}