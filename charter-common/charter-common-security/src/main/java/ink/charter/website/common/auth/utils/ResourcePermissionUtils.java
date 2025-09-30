package ink.charter.website.common.auth.utils;

import ink.charter.website.common.core.exception.ResourcePermissionException;
import ink.charter.website.common.auth.model.LoginUser;
import ink.charter.website.common.auth.service.ResourcePermissionService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 资源权限工具类
 * 提供便捷的权限检查方法
 *
 * @author charter
 * @create 2025/09/30
 */
@Component
public class ResourcePermissionUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static ResourcePermissionService resourcePermissionService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ResourcePermissionUtils.applicationContext = applicationContext;
    }

    /**
     * 获取资源权限服务
     */
    private static ResourcePermissionService getResourcePermissionService() {
        if (resourcePermissionService == null && applicationContext != null) {
            try {
                resourcePermissionService = applicationContext.getBean(ResourcePermissionService.class);
            } catch (Exception e) {
                // 如果没有找到ResourcePermissionService，返回null
                return null;
            }
        }
        return resourcePermissionService;
    }

    /**
     * 检查当前用户是否有指定资源权限
     *
     * @param resourceCode 资源代码
     * @return 是否有权限
     */
    public static boolean hasResourcePermission(String resourceCode) {
        ResourcePermissionService service = getResourcePermissionService();
        if (service == null) {
            return true; // 如果服务不可用，默认允许访问
        }
        LoginUser loginUser = SecurityUtils.getCurrentUser();
        if (loginUser == null) {
            return false;
        }
        return service.hasResourcePermission(loginUser, resourceCode);
    }

    /**
     * 检查当前用户是否有任意一个资源权限
     *
     * @param resourceCodes 资源代码数组
     * @return 是否有权限
     */
    public static boolean hasAnyResourcePermission(String... resourceCodes) {
        ResourcePermissionService service = getResourcePermissionService();
        if (service == null) {
            return true;
        }
        LoginUser loginUser = SecurityUtils.getCurrentUser();
        if (loginUser == null) {
            return false;
        }
        return service.hasAnyResourcePermission(loginUser, resourceCodes);
    }

    /**
     * 检查当前用户是否有所有资源权限
     *
     * @param resourceCodes 资源代码数组
     * @return 是否有权限
     */
    public static boolean hasAllResourcePermissions(String... resourceCodes) {
        ResourcePermissionService service = getResourcePermissionService();
        if (service == null) {
            return true;
        }
        LoginUser loginUser = SecurityUtils.getCurrentUser();
        if (loginUser == null) {
            return false;
        }
        return service.hasAllResourcePermissions(loginUser, resourceCodes);
    }

    /**
     * 检查指定用户是否有资源权限
     *
     * @param user 用户信息
     * @param resourceCode 资源代码
     * @return 是否有权限
     */
    public static boolean hasResourcePermission(LoginUser user, String resourceCode) {
        ResourcePermissionService service = getResourcePermissionService();
        if (service == null) {
            return true;
        }
        return service.hasResourcePermission(user, resourceCode);
    }

    /**
     * 检查指定用户是否有任意一个资源权限
     *
     * @param user 用户信息
     * @param resourceCodes 资源代码数组
     * @return 是否有权限
     */
    public static boolean hasAnyResourcePermission(LoginUser user, String... resourceCodes) {
        ResourcePermissionService service = getResourcePermissionService();
        if (service == null) {
            return true;
        }
        return service.hasAnyResourcePermission(user, resourceCodes);
    }

    /**
     * 检查指定用户是否有所有资源权限
     *
     * @param user 用户信息
     * @param resourceCodes 资源代码数组
     * @return 是否有权限
     */
    public static boolean hasAllResourcePermissions(LoginUser user, String... resourceCodes) {
        ResourcePermissionService service = getResourcePermissionService();
        if (service == null) {
            return true;
        }
        return service.hasAllResourcePermissions(user, resourceCodes);
    }

    /**
     * 要求当前用户有指定资源权限，否则抛出异常
     *
     * @param resourceCode 资源代码
     * @throws ResourcePermissionException 权限不足异常
     */
    public static void requireResourcePermission(String resourceCode) {
        if (!hasResourcePermission(resourceCode)) {
            throw ResourcePermissionException.resourceDenied(resourceCode);
        }
    }

    /**
     * 要求当前用户有任意一个资源权限，否则抛出异常
     *
     * @param resourceCodes 资源代码数组
     * @throws ResourcePermissionException 权限不足异常
     */
    public static void requireAnyResourcePermission(String... resourceCodes) {
        if (!hasAnyResourcePermission(resourceCodes)) {
            throw ResourcePermissionException.permissionDenied("访问被拒绝：缺少任意一个资源权限 " + String.join(", ", resourceCodes));
        }
    }

    /**
     * 要求当前用户有所有资源权限，否则抛出异常
     *
     * @param resourceCodes 资源代码数组
     * @throws ResourcePermissionException 权限不足异常
     */
    public static void requireAllResourcePermissions(String... resourceCodes) {
        if (!hasAllResourcePermissions(resourceCodes)) {
            throw ResourcePermissionException.permissionDenied("访问被拒绝：缺少所有资源权限 " + String.join(", ", resourceCodes));
        }
    }

    /**
     * 要求指定用户有资源权限，否则抛出异常
     *
     * @param user 用户信息
     * @param resourceCode 资源代码
     * @throws ResourcePermissionException 权限不足异常
     */
    public static void requireResourcePermission(LoginUser user, String resourceCode) {
        if (!hasResourcePermission(user, resourceCode)) {
            throw ResourcePermissionException.permissionDenied("访问被拒绝：用户缺少资源权限 [" + resourceCode + "]");
        }
    }

    /**
     * 要求指定用户有任意一个资源权限，否则抛出异常
     *
     * @param user 用户信息
     * @param resourceCodes 资源代码数组
     * @throws ResourcePermissionException 权限不足异常
     */
    public static void requireAnyResourcePermission(LoginUser user, String... resourceCodes) {
        if (!hasAnyResourcePermission(user, resourceCodes)) {
            throw ResourcePermissionException.permissionDenied("访问被拒绝：用户缺少任意一个资源权限 " + String.join(", ", resourceCodes));
        }
    }

    /**
     * 要求指定用户有所有资源权限，否则抛出异常
     *
     * @param user 用户信息
     * @param resourceCodes 资源代码数组
     * @throws ResourcePermissionException 权限不足异常
     */
    public static void requireAllResourcePermissions(LoginUser user, String... resourceCodes) {
        if (!hasAllResourcePermissions(user, resourceCodes)) {
            throw ResourcePermissionException.permissionDenied("访问被拒绝：用户缺少所有资源权限 " + String.join(", ", resourceCodes));
        }
    }
}