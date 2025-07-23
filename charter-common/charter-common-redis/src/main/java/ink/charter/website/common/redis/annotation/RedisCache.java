package ink.charter.website.common.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存注解
 * 用于方法级别的缓存控制
 *
 * @author charter
 * @create 2025/07/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCache {

    /**
     * 缓存键前缀
     * 支持SpEL表达式
     */
    String key() default "";

    /**
     * 缓存过期时间
     * 默认1小时
     */
    long expire() default 3600;

    /**
     * 时间单位
     * 默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否允许缓存空值
     * 默认false
     */
    boolean cacheNull() default false;

    /**
     * 条件表达式
     * 支持SpEL表达式，当条件为true时才进行缓存
     */
    String condition() default "";

    /**
     * 排除条件表达式
     * 支持SpEL表达式，当条件为true时不进行缓存
     */
    String unless() default "";
}