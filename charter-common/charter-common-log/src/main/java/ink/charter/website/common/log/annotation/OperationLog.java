package ink.charter.website.common.log.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 *
 * @author charter
 * @create 2025/07/17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 操作类型
     */
    String type() default "";

    /**
     * 操作描述
     */
    String description() default "";

    /**
     * 是否记录请求参数
     */
    boolean recordParams() default true;

    /**
     * 是否记录响应数据
     */
    boolean recordResponse() default true;

    /**
     * 是否异步记录日志
     */
    boolean async() default true;

    /**
     * 忽略的参数名称（敏感信息）
     */
    String[] ignoreParams() default {"password", "oldPassword", "newPassword", "confirmPassword", "token", "accessToken", "refreshToken"};
}