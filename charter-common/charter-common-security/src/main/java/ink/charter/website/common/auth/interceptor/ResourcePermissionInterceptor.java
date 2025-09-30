package ink.charter.website.common.auth.interceptor;

import ink.charter.website.common.auth.annotation.RequirePermission;
import ink.charter.website.common.auth.annotation.RequireResource;
import ink.charter.website.common.auth.model.LoginUser;
import ink.charter.website.common.auth.service.ResourcePermissionService;
import ink.charter.website.common.auth.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * 资源权限拦截器
 * 基于注解和URL配置进行权限验证
 *
 * @author charter
 * @create 2025/09/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourcePermissionInterceptor implements HandlerInterceptor {

    private final ResourcePermissionService resourcePermissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只处理Controller方法
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 获取当前登录用户
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            // 未登录用户，交给Spring Security处理
            return true;
        }

        Method method = handlerMethod.getMethod();
        Class<?> controllerClass = handlerMethod.getBeanType();

        // 检查方法级别的权限注解
        if (!checkMethodPermissions(method, currentUser, request, response)) {
            return false;
        }

        // 检查类级别的权限注解
        if (!checkClassPermissions(controllerClass, currentUser, request, response)) {
            return false;
        }

        // 检查URL级别的权限配置
        if (!checkUrlPermissions(request, currentUser, response)) {
            return false;
        }

        return true;
    }

    /**
     * 检查方法级别的权限注解
     */
    private boolean checkMethodPermissions(Method method, LoginUser currentUser, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 检查 @RequireResource 注解
        RequireResource requireResource = AnnotationUtils.findAnnotation(method, RequireResource.class);
        if (requireResource != null && requireResource.enabled()) {
            if (!validateResourcePermission(requireResource, currentUser)) {
                sendAccessDeniedResponse(response, requireResource.message());
                return false;
            }
        }

        // 检查 @RequirePermission 注解
        RequirePermission requirePermission = AnnotationUtils.findAnnotation(method, RequirePermission.class);
        if (requirePermission != null && requirePermission.enabled()) {
            if (!validateGeneralPermission(requirePermission, currentUser)) {
                sendAccessDeniedResponse(response, requirePermission.message());
                return false;
            }
        }

        return true;
    }

    /**
     * 检查类级别的权限注解
     */
    private boolean checkClassPermissions(Class<?> controllerClass, LoginUser currentUser, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 检查 @RequireResource 注解
        RequireResource requireResource = AnnotationUtils.findAnnotation(controllerClass, RequireResource.class);
        if (requireResource != null && requireResource.enabled()) {
            if (!validateResourcePermission(requireResource, currentUser)) {
                sendAccessDeniedResponse(response, requireResource.message());
                return false;
            }
        }

        // 检查 @RequirePermission 注解
        RequirePermission requirePermission = AnnotationUtils.findAnnotation(controllerClass, RequirePermission.class);
        if (requirePermission != null && requirePermission.enabled()) {
            if (!validateGeneralPermission(requirePermission, currentUser)) {
                sendAccessDeniedResponse(response, requirePermission.message());
                return false;
            }
        }

        return true;
    }

    /**
     * 检查URL级别的权限配置
     */
    private boolean checkUrlPermissions(HttpServletRequest request, LoginUser currentUser, HttpServletResponse response) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // 检查是否需要权限验证
        if (!resourcePermissionService.validateRequestPermission(requestURI, method, currentUser)) {
            sendAccessDeniedResponse(response, "访问被拒绝：缺少访问该资源的权限");
            return false;
        }

        return true;
    }

    /**
     * 验证资源权限注解
     */
    private boolean validateResourcePermission(RequireResource requireResource, LoginUser currentUser) {
        String[] resourceCodes = requireResource.value().length > 0 ? requireResource.value() : requireResource.codes();
        
        if (resourceCodes.length == 0) {
            log.warn("@RequireResource annotation found but no resource codes specified");
            return true;
        }

        if (requireResource.requireAll()) {
            // 需要所有权限
            return resourcePermissionService.hasAllResourcePermissions(currentUser, resourceCodes);
        } else {
            // 需要任意一个权限
            return resourcePermissionService.hasAnyResourcePermission(currentUser, resourceCodes);
        }
    }

    /**
     * 验证通用权限注解
     */
    private boolean validateGeneralPermission(RequirePermission requirePermission, LoginUser currentUser) {
        String[] roles = requirePermission.roles();
        String[] resources = requirePermission.resources();

        boolean hasRolePermission = true;
        boolean hasResourcePermission = true;

        // 检查角色权限
        if (roles.length > 0) {
            if (requirePermission.requireAllRoles()) {
                hasRolePermission = currentUser.hasAllRoles(roles);
            } else {
                hasRolePermission = currentUser.hasAnyRole(roles);
            }
        }

        // 检查资源权限
        if (resources.length > 0) {
            if (requirePermission.requireAllResources()) {
                hasResourcePermission = resourcePermissionService.hasAllResourcePermissions(currentUser, resources);
            } else {
                hasResourcePermission = resourcePermissionService.hasAnyResourcePermission(currentUser, resources);
            }
        }

        // 根据配置决定角色和资源权限的关系
        if (requirePermission.requireBoth()) {
            // 角色和资源权限都需要满足
            return hasRolePermission && hasResourcePermission;
        } else {
            // 满足角色或资源权限任意一种即可
            if (roles.length > 0 && resources.length > 0) {
                return hasRolePermission || hasResourcePermission;
            } else if (roles.length > 0) {
                return hasRolePermission;
            } else if (resources.length > 0) {
                return hasResourcePermission;
            } else {
                return true;
            }
        }
    }

    /**
     * 发送访问拒绝响应
     */
    private void sendAccessDeniedResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        String jsonResponse = String.format(
            "{\"code\":403,\"message\":\"%s\",\"data\":null,\"timestamp\":\"%s\"}",
            message,
            System.currentTimeMillis()
        );
        
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}