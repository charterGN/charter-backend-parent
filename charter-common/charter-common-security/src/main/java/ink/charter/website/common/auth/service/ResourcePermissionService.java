package ink.charter.website.common.auth.service;

import ink.charter.website.common.auth.model.LoginUser;

import java.util.Set;

/**
 * 资源权限服务接口
 * 提供资源权限验证的核心功能
 *
 * @author charter
 * @create 2025/09/30
 */
public interface ResourcePermissionService {

    /**
     * 检查用户是否有指定资源的权限
     *
     * @param userId       用户ID
     * @param resourceCode 资源编码
     * @return 是否有权限
     */
    boolean hasResourcePermission(Long userId, String resourceCode);

    /**
     * 检查用户是否有指定资源的权限
     *
     * @param loginUser    登录用户信息
     * @param resourceCode 资源编码
     * @return 是否有权限
     */
    boolean hasResourcePermission(LoginUser loginUser, String resourceCode);

    /**
     * 检查用户是否有任意一个指定资源的权限
     *
     * @param userId        用户ID
     * @param resourceCodes 资源编码列表
     * @return 是否有权限
     */
    boolean hasAnyResourcePermission(Long userId, String... resourceCodes);

    /**
     * 检查用户是否有任意一个指定资源的权限
     *
     * @param loginUser     登录用户信息
     * @param resourceCodes 资源编码列表
     * @return 是否有权限
     */
    boolean hasAnyResourcePermission(LoginUser loginUser, String... resourceCodes);

    /**
     * 检查用户是否有所有指定资源的权限
     *
     * @param userId        用户ID
     * @param resourceCodes 资源编码列表
     * @return 是否有权限
     */
    boolean hasAllResourcePermissions(Long userId, String... resourceCodes);

    /**
     * 检查用户是否有所有指定资源的权限
     *
     * @param loginUser     登录用户信息
     * @param resourceCodes 资源编码列表
     * @return 是否有权限
     */
    boolean hasAllResourcePermissions(LoginUser loginUser, String... resourceCodes);

    /**
     * 获取用户的所有资源权限
     *
     * @param userId 用户ID
     * @return 资源权限集合
     */
    Set<String> getUserResourcePermissions(Long userId);

    /**
     * 检查当前请求的URL和HTTP方法是否需要权限验证
     *
     * @param url    请求URL
     * @param method HTTP方法
     * @return 需要的资源编码，如果不需要验证则返回null
     */
    String getRequiredResourceCode(String url, String method);

    /**
     * 验证当前请求是否有权限访问
     *
     * @param url    请求URL
     * @param method HTTP方法
     * @param userId 用户ID
     * @return 是否有权限
     */
    boolean validateRequestPermission(String url, String method, Long userId);

    /**
     * 验证当前请求是否有权限访问
     *
     * @param url       请求URL
     * @param method    HTTP方法
     * @param loginUser 登录用户信息
     * @return 是否有权限
     */
    boolean validateRequestPermission(String url, String method, LoginUser loginUser);

    /**
     * 刷新用户权限缓存
     *
     * @param userId 用户ID
     */
    void refreshUserPermissions(Long userId);

    /**
     * 清除用户权限缓存
     *
     * @param userId 用户ID
     */
    void clearUserPermissions(Long userId);

    /**
     * 清除所有用户权限缓存
     */
    void clearAllUserPermissions();
}