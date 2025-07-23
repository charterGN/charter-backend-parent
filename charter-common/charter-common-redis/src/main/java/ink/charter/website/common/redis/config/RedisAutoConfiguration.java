package ink.charter.website.common.redis.config;

import ink.charter.website.common.redis.aspect.RedisCacheAspect;
import ink.charter.website.common.redis.aspect.RedisCacheEvictAspect;
import ink.charter.website.common.redis.service.RedisService;
import ink.charter.website.common.redis.service.impl.RedisServiceImpl;
import ink.charter.website.common.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis自动配置类
 * 自动装配Redis相关组件
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(RedisTemplate.class)
@ConditionalOnProperty(prefix = "spring.data.redis", name = "host")
@Import({RedisConfig.class})
public class RedisAutoConfiguration {

    /**
     * 注册Redis服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisService redisService(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        log.info("初始化Redis服务");
        return new RedisServiceImpl(redisTemplate, stringRedisTemplate);
    }

    /**
     * 注册Redis工具类
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisUtils redisUtils() {
        log.info("初始化Redis工具类");
        return new RedisUtils();
    }

    /**
     * 注册Redis缓存切面
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.redis.cache", name = "enabled", havingValue = "true", matchIfMissing = true)
    public RedisCacheAspect redisCacheAspect(RedisService redisService) {
        log.info("初始化Redis缓存切面");
        return new RedisCacheAspect(redisService);
    }

    /**
     * 注册Redis缓存清除切面
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.redis.cache", name = "enabled", havingValue = "true", matchIfMissing = true)
    public RedisCacheEvictAspect redisCacheEvictAspect(RedisService redisService, RedisTemplate<String, Object> redisTemplate) {
        log.info("初始化Redis缓存清除切面");
        return new RedisCacheEvictAspect(redisService, redisTemplate);
    }
}