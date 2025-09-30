package ink.charter.website.common.auth.annotation;

import java.lang.annotation.*;

/**
 * 通用权限注解
 * 支持角色和资源权限的组合验证
 *
 * @author charter
 * @create 2025/09/30
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 需要的角色列表
     */
    String[] roles() default {};

    /**
     * 需要的资源权限列表
     */
    String[] resources() default {};

    /**
     * 角色验证逻辑
     * true: 需要满足所有指定角色（AND关系）
     * false: 满足任意一个角色即可（OR关系）
     */
    boolean requireAllRoles() default false;

    /**
     * 资源权限验证逻辑
     * true: 需要满足所有指定资源权限（AND关系）
     * false: 满足任意一个资源权限即可（OR关系）
     */
    boolean requireAllResources() default false;

    /**
     * 角色和资源权限之间的关系
     * true: 角色和资源权限都需要满足（AND关系）
     * false: 满足角色或资源权限任意一种即可（OR关系）
     */
    boolean requireBoth() default false;

    /**
     * 权限验证失败时的提示信息
     */
    String message() default "访问被拒绝：权限不足";

    /**
     * 是否启用权限验证
     */
    boolean enabled() default true;
}