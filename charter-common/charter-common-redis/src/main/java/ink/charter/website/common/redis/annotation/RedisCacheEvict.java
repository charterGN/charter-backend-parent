package ink.charter.website.common.redis.annotation;

import java.lang.annotation.*;

/**
 * Redis缓存清除注解
 * 用于清除指定的缓存
 *
 * @author charter
 * @create 2025/07/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCacheEvict {

    /**
     * 缓存键
     * 支持SpEL表达式
     */
    String key() default "";

    /**
     * 缓存键模式
     * 支持通配符，用于批量删除
     */
    String keyPattern() default "";

    /**
     * 是否清除所有缓存
     * 默认false
     */
    boolean allEntries() default false;

    /**
     * 是否在方法执行前清除缓存
     * 默认false（方法执行后清除）
     */
    boolean beforeInvocation() default false;

    /**
     * 条件表达式
     * 支持SpEL表达式，当条件为true时才清除缓存
     */
    String condition() default "";
}