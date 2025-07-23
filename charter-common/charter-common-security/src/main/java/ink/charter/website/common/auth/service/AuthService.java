package ink.charter.website.common.auth.service;

import ink.charter.website.common.auth.model.LoginResponse;
import ink.charter.website.common.auth.model.LoginUser;
import ink.charter.website.common.core.common.Result;

/**
 * 认证服务接口
 *
 * @author charter
 * @create 2025/07/17
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param username 用户名或邮箱
     * @param password 密码
     * @param ip       登录IP
     * @param userAgent 用户代理
     * @return 登录结果
     */
    Result<LoginResponse> login(String username, String password, String ip, String userAgent);

    /**
     * 用户登出
     *
     * @param token 访问令牌
     * @return 登出是否成功
     */
    boolean logout(String token);

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    Result<LoginResponse> refreshToken(String refreshToken);

    /**
     * 验证令牌
     *
     * @param token 访问令牌
     * @return 令牌是否有效
     */
    boolean validateToken(String token);

    /**
     * 踢出用户
     *
     * @param userId 用户ID
     * @return 踢出是否成功
     */
    boolean kickOutUser(Long userId);

    /**
     * 检查登录限制
     *
     * @param usernameOrEmail 用户名或邮箱
     * @return 是否被限制
     */
    boolean isLoginRestricted(String usernameOrEmail);

    /**
     * 记录登录失败
     *
     * @param usernameOrEmail 用户名或邮箱
     */
    void recordLoginFailure(String usernameOrEmail);

    /**
     * 清除登录限制
     *
     * @param usernameOrEmail 用户名或邮箱
     */
    void clearLoginRestriction(String usernameOrEmail);


}