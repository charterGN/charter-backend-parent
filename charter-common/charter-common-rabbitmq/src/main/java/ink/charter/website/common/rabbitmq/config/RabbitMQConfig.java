package ink.charter.website.common.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * RabbitMQ配置类
 * 配置消息转换器、重试策略、监听器容器等
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@Configuration
public class RabbitMQConfig {

    /**
     * 配置RabbitAdmin
     * 用于声明队列、交换机、绑定等
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * 配置消息转换器
     * 使用Jackson2JsonMessageConverter进行JSON序列化
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置RabbitTemplate
     * 设置消息转换器、确认回调、返回回调等
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        
        // 设置发送确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送成功: correlationData={}", correlationData);
            } else {
                log.error("消息发送失败: correlationData={}, cause={}", correlationData, cause);
            }
        });
        
        // 设置返回回调（当消息无法路由到队列时触发）
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("消息路由失败: message={}, replyCode={}, replyText={}, exchange={}, routingKey={}",
                    returned.getMessage(), returned.getReplyCode(), returned.getReplyText(),
                    returned.getExchange(), returned.getRoutingKey());
        });
        
        // 启用强制模式，确保消息能够被路由
        rabbitTemplate.setMandatory(true);
        
        return rabbitTemplate;
    }

    /**
     * 配置监听器容器工厂
     * 设置并发数、预取数量、确认模式等
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                              MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        
        // 设置并发消费者数量
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(5);
        
        // 设置预取数量
        factory.setPrefetchCount(1);
        
        // 设置确认模式为手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        
        // 设置重试模板
        factory.setRetryTemplate(retryTemplate());
        
        return factory;
    }

    /**
     * 配置重试模板
     * 设置重试次数和退避策略
     */
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        
        // 设置重试策略
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        
        // 设置退避策略
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        
        return retryTemplate;
    }

    /**
     * 创建死信交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("charter.dlx.exchange", true, false);
    }

    /**
     * 创建死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("charter.dlx.queue").build();
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("dlx");
    }
}