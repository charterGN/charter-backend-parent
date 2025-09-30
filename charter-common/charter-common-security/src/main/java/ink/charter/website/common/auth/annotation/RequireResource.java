package ink.charter.website.common.auth.annotation;

import java.lang.annotation.*;

/**
 * 资源权限注解
 * 用于标记需要特定资源权限的方法或类
 *
 * @author charter
 * @create 2025/09/30
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireResource {

    /**
     * 资源编码
     * 支持多个资源编码，满足其中一个即可访问
     */
    String[] value() default {};

    /**
     * 资源编码（别名）
     */
    String[] codes() default {};

    /**
     * 是否需要所有资源权限都满足
     * true: 需要满足所有指定的资源权限（AND关系）
     * false: 满足任意一个资源权限即可（OR关系）
     */
    boolean requireAll() default false;

    /**
     * 权限验证失败时的提示信息
     */
    String message() default "访问被拒绝：缺少必要的资源权限";

    /**
     * 是否启用权限验证
     * 可用于临时禁用某个接口的权限验证
     */
    boolean enabled() default true;
}