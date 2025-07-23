package ink.charter.website.common.auth.service;

import ink.charter.website.common.auth.model.LoginUser;

import java.time.LocalDateTime;

/**
 * Token服务接口
 *
 * @author charter
 * @create 2025/07/17
 */
public interface TokenService {

    /**
     * 创建令牌
     *
     * @param loginUser 登录用户信息
     * @return 访问令牌
     */
    String createToken(LoginUser loginUser);

    /**
     * 创建刷新令牌
     *
     * @param loginUser 登录用户信息
     * @return 刷新令牌
     */
    String createRefreshToken(LoginUser loginUser);

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @return 令牌是否有效
     */
    boolean verifyToken(String token);
    
    /**
     * 从令牌获取登录用户信息
     *
     * @param token 令牌
     * @return 登录用户信息
     */
    LoginUser getLoginUserFromToken(String token);

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    String refreshAccessToken(String refreshToken);

    /**
     * 删除令牌
     *
     * @param token 令牌
     */
    void deleteToken(String token);

    /**
     * 删除用户所有令牌
     *
     * @param userId 用户ID
     */
    void deleteUserTokens(Long userId);

    /**
     * 获取令牌过期时间
     *
     * @param token 令牌
     * @return 过期时间
     */
    LocalDateTime getTokenExpireTime(String token);

    /**
     * 延长令牌有效期
     *
     * @param token 令牌
     * @param expireSeconds 延长时间（秒）
     */
    void extendTokenExpire(String token, long expireSeconds);

    /**
     * 检查令牌是否在黑名单中
     *
     * @param token 令牌
     * @return 是否在黑名单
     */
    boolean isTokenBlacklisted(String token);

    /**
     * 将令牌加入黑名单
     *
     * @param token 令牌
     */
    void addTokenToBlacklist(String token);

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    Long getUserIdFromToken(String token);

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    String getUsernameFromToken(String token);

    /**
     * 检查令牌是否即将过期
     *
     * @param token 令牌
     * @param thresholdSeconds 阈值时间（秒）
     * @return 是否即将过期
     */
    boolean isTokenExpiringSoon(String token, long thresholdSeconds);

    /**
     * 获取用户在线令牌数量
     *
     * @param userId 用户ID
     * @return 令牌数量
     */
    int getUserTokenCount(Long userId);

    /**
     * 清理过期令牌
     */
    void cleanExpiredTokens();
}