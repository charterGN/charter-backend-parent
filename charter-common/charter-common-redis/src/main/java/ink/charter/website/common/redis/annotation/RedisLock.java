package ink.charter.website.common.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁注解
 * 用于方法级别的分布式锁控制
 *
 * @author charter
 * @create 2025/07/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * 锁的键
     * 支持SpEL表达式
     */
    String key();

    /**
     * 锁的过期时间
     * 默认30秒
     */
    long expire() default 30;

    /**
     * 时间单位
     * 默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 获取锁的等待时间
     * 默认0（不等待）
     */
    long waitTime() default 0;

    /**
     * 获取锁失败时的处理策略
     */
    LockFailStrategy failStrategy() default LockFailStrategy.THROW_EXCEPTION;

    /**
     * 获取锁失败时的错误消息
     */
    String failMessage() default "获取分布式锁失败";

    /**
     * 锁失败处理策略枚举
     */
    enum LockFailStrategy {
        /**
         * 抛出异常
         */
        THROW_EXCEPTION,
        
        /**
         * 返回null
         */
        RETURN_NULL,
        
        /**
         * 忽略（继续执行）
         */
        IGNORE
    }
}