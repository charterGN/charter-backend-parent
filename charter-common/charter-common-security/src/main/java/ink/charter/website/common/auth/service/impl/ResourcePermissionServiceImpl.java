package ink.charter.website.common.auth.service.impl;

import ink.charter.website.common.auth.model.LoginUser;
import ink.charter.website.common.auth.service.ResourcePermissionService;
import ink.charter.website.common.auth.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源权限服务实现类
 *
 * @author charter
 * @create 2025/09/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourcePermissionServiceImpl implements ResourcePermissionService {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    // 资源URL映射缓存：URL+Method -> ResourceCode
    private final ConcurrentHashMap<String, String> resourceMappingCache = new ConcurrentHashMap<>();
    
    // 用户服务，通过反射调用，避免循环依赖
    @Autowired(required = false)
    private Object userService;

    @Override
    public boolean hasResourcePermission(Long userId, String resourceCode) {
        if (userId == null || !StringUtils.hasText(resourceCode)) {
            return false;
        }
        
        Set<String> userPermissions = getUserResourcePermissions(userId);
        return userPermissions.contains(resourceCode);
    }

    @Override
    public boolean hasResourcePermission(LoginUser loginUser, String resourceCode) {
        if (loginUser == null || !StringUtils.hasText(resourceCode)) {
            return false;
        }
        
        // 优先使用LoginUser中缓存的权限信息
        if (loginUser.getPermissions() != null) {
            return loginUser.getPermissions().contains(resourceCode);
        }
        
        // 如果LoginUser中没有权限信息，则查询数据库
        return hasResourcePermission(loginUser.getUserId(), resourceCode);
    }

    @Override
    public boolean hasAnyResourcePermission(Long userId, String... resourceCodes) {
        if (userId == null || resourceCodes == null || resourceCodes.length == 0) {
            return false;
        }
        
        Set<String> userPermissions = getUserResourcePermissions(userId);
        for (String resourceCode : resourceCodes) {
            if (StringUtils.hasText(resourceCode) && userPermissions.contains(resourceCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAnyResourcePermission(LoginUser loginUser, String... resourceCodes) {
        if (loginUser == null || resourceCodes == null || resourceCodes.length == 0) {
            return false;
        }
        
        // 优先使用LoginUser中缓存的权限信息
        if (loginUser.getPermissions() != null) {
            for (String resourceCode : resourceCodes) {
                if (StringUtils.hasText(resourceCode) && loginUser.getPermissions().contains(resourceCode)) {
                    return true;
                }
            }
            return false;
        }
        
        // 如果LoginUser中没有权限信息，则查询数据库
        return hasAnyResourcePermission(loginUser.getUserId(), resourceCodes);
    }

    @Override
    public boolean hasAllResourcePermissions(Long userId, String... resourceCodes) {
        if (userId == null || resourceCodes == null || resourceCodes.length == 0) {
            return false;
        }
        
        Set<String> userPermissions = getUserResourcePermissions(userId);
        for (String resourceCode : resourceCodes) {
            if (!StringUtils.hasText(resourceCode) || !userPermissions.contains(resourceCode)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasAllResourcePermissions(LoginUser loginUser, String... resourceCodes) {
        if (loginUser == null || resourceCodes == null || resourceCodes.length == 0) {
            return false;
        }
        
        // 优先使用LoginUser中缓存的权限信息
        if (loginUser.getPermissions() != null) {
            for (String resourceCode : resourceCodes) {
                if (!StringUtils.hasText(resourceCode) || !loginUser.getPermissions().contains(resourceCode)) {
                    return false;
                }
            }
            return true;
        }
        
        // 如果LoginUser中没有权限信息，则查询数据库
        return hasAllResourcePermissions(loginUser.getUserId(), resourceCodes);
    }

    @Override
    @Cacheable(value = "userResourcePermissions", key = "#userId", unless = "#result == null")
    public Set<String> getUserResourcePermissions(Long userId) {
        if (userId == null) {
            return Collections.emptySet();
        }
        
        if (userService == null) {
            log.warn("UserService not found, cannot get user permissions for userId: {}", userId);
            return Collections.emptySet();
        }
        
        try {
            // 通过反射调用UserService的getUserPermissions方法
            Method method = userService.getClass().getMethod("getUserPermissions", Long.class);
            @SuppressWarnings("unchecked")
            Set<String> permissions = (Set<String>) method.invoke(userService, userId);
            return permissions != null ? permissions : Collections.emptySet();
        } catch (Exception e) {
            log.error("Failed to get user permissions for userId: {}", userId, e);
            return Collections.emptySet();
        }
    }

    @Override
    public String getRequiredResourceCode(String url, String method) {
        if (!StringUtils.hasText(url) || !StringUtils.hasText(method)) {
            return null;
        }
        
        String key = method.toUpperCase() + ":" + url;
        
        // 先从缓存中查找
        String resourceCode = resourceMappingCache.get(key);
        if (resourceCode != null) {
            return resourceCode.isEmpty() ? null : resourceCode;
        }
        
        // 查询数据库中的资源配置
        resourceCode = queryResourceCodeFromDatabase(url, method);
        
        // 缓存结果（空字符串表示不需要权限验证）
        resourceMappingCache.put(key, resourceCode != null ? resourceCode : "");
        
        return resourceCode;
    }

    @Override
    public boolean validateRequestPermission(String url, String method, Long userId) {
        String resourceCode = getRequiredResourceCode(url, method);
        if (resourceCode == null) {
            // 不需要权限验证的接口
            return true;
        }
        
        return hasResourcePermission(userId, resourceCode);
    }

    @Override
    public boolean validateRequestPermission(String url, String method, LoginUser loginUser) {
        String resourceCode = getRequiredResourceCode(url, method);
        if (resourceCode == null) {
            // 不需要权限验证的接口
            return true;
        }
        
        return hasResourcePermission(loginUser, resourceCode);
    }

    @Override
    @CacheEvict(value = "userResourcePermissions", key = "#userId")
    public void refreshUserPermissions(Long userId) {
        log.debug("Refreshed user permissions cache for userId: {}", userId);
    }

    @Override
    @CacheEvict(value = "userResourcePermissions", key = "#userId")
    public void clearUserPermissions(Long userId) {
        log.debug("Cleared user permissions cache for userId: {}", userId);
    }

    @Override
    @CacheEvict(value = "userResourcePermissions", allEntries = true)
    public void clearAllUserPermissions() {
        log.debug("Cleared all user permissions cache");
        // 同时清除资源映射缓存
        resourceMappingCache.clear();
    }

    /**
     * 从数据库查询资源编码
     */
    private String queryResourceCodeFromDatabase(String url, String method) {
        if (userService == null) {
            return null;
        }
        
        try {
            // 通过反射调用查询资源的方法
            Method queryMethod = userService.getClass().getMethod("getResourceCodeByUrlAndMethod", String.class, String.class);
            return (String) queryMethod.invoke(userService, url, method);
        } catch (NoSuchMethodException e) {
            log.debug("Method getResourceCodeByUrlAndMethod not found in userService, resource permission validation disabled");
            return null;
        } catch (Exception e) {
            log.error("Failed to query resource code for url: {}, method: {}", url, method, e);
            return null;
        }
    }

    /**
     * 检查当前用户是否有权限
     */
    public boolean checkCurrentUserPermission(String resourceCode) {
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return hasResourcePermission(currentUser, resourceCode);
    }

    /**
     * 检查当前用户是否有任意一个权限
     */
    public boolean checkCurrentUserAnyPermission(String... resourceCodes) {
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return hasAnyResourcePermission(currentUser, resourceCodes);
    }

    /**
     * 检查当前用户是否有所有权限
     */
    public boolean checkCurrentUserAllPermissions(String... resourceCodes) {
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return hasAllResourcePermissions(currentUser, resourceCodes);
    }
}