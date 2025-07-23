package ink.charter.website.common.rabbitmq.config;

import ink.charter.website.common.rabbitmq.aspect.RabbitConsumerAspect;
import ink.charter.website.common.rabbitmq.aspect.RabbitProducerAspect;
import ink.charter.website.common.rabbitmq.service.RabbitMQService;
import ink.charter.website.common.rabbitmq.service.impl.RabbitMQServiceImpl;
import ink.charter.website.common.rabbitmq.utils.RabbitMQUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * RabbitMQ自动配置类
 * 自动装配RabbitMQ相关组件
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(RabbitTemplate.class)
@ConditionalOnProperty(prefix = "spring.rabbitmq", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({RabbitMQConfig.class})
public class RabbitMQAutoConfiguration {

    /**
     * 注册RabbitMQ服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    public RabbitMQService rabbitMQService(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin, ConnectionFactory connectionFactory) {
        log.info("初始化RabbitMQ服务");
        return new RabbitMQServiceImpl(rabbitTemplate, rabbitAdmin, connectionFactory);
    }

    /**
     * 注册RabbitMQ工具类
     */
    @Bean
    @ConditionalOnMissingBean
    public RabbitMQUtils rabbitMQUtils() {
        log.info("初始化RabbitMQ工具类");
        return new RabbitMQUtils();
    }

    /**
     * 注册生产者切面
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.rabbitmq.producer", name = "enabled", havingValue = "true", matchIfMissing = true)
    public RabbitProducerAspect rabbitProducerAspect(RabbitMQService rabbitMQService) {
        log.info("初始化RabbitMQ生产者切面");
        return new RabbitProducerAspect(rabbitMQService);
    }

    /**
     * 注册消费者切面
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.rabbitmq.consumer", name = "enabled", havingValue = "true", matchIfMissing = true)
    public RabbitConsumerAspect rabbitConsumerAspect(RabbitMQService rabbitMQService, RabbitAdmin rabbitAdmin) {
        log.info("初始化RabbitMQ消费者切面");
        return new RabbitConsumerAspect(rabbitMQService, rabbitAdmin);
    }
}