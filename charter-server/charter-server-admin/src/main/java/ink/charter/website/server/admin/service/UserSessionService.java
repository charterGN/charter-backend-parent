package ink.charter.website.server.admin.service;

import ink.charter.website.common.core.entity.sys.SysUserSessionEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户会话服务接口
 *
 * @author charter
 * @create 2025/07/17
 */
public interface UserSessionService {

    /**
     * 创建用户会话
     *
     * @param userId 用户ID
     * @param sessionToken 会话Token
     * @param refreshToken 刷新Token
     * @param loginIp 登录IP
     * @param userAgent 用户代理
     * @param expireTime 过期时间
     * @return 会话信息
     */
    SysUserSessionEntity createSession(Long userId, String sessionToken, String refreshToken, 
                                      String loginIp, String userAgent, LocalDateTime expireTime);

    /**
     * 根据会话Token查询会话信息
     *
     * @param sessionToken 会话Token
     * @return 会话信息
     */
    SysUserSessionEntity getSessionByToken(String sessionToken);

    /**
     * 根据刷新Token查询会话信息
     *
     * @param refreshToken 刷新Token
     * @return 会话信息
     */
    SysUserSessionEntity getSessionByRefreshToken(String refreshToken);

    /**
     * 查询用户的有效会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<SysUserSessionEntity> getActiveSessionsByUserId(Long userId);

    /**
     * 查询用户的所有会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<SysUserSessionEntity> getSessionsByUserId(Long userId);

    /**
     * 使会话失效
     *
     * @param sessionToken 会话Token
     */
    void invalidateSession(String sessionToken);

    /**
     * 使用户的所有会话失效
     *
     * @param userId 用户ID
     */
    void invalidateUserSessions(Long userId);

    /**
     * 使用户的其他会话失效（除了当前会话）
     *
     * @param userId 用户ID
     * @param currentSessionToken 当前会话Token
     */
    void invalidateOtherUserSessions(Long userId, String currentSessionToken);

    /**
     * 更新会话过期时间
     *
     * @param sessionToken 会话Token
     * @param expireTime 过期时间
     */
    void updateExpireTime(String sessionToken, LocalDateTime expireTime);

    /**
     * 清理过期会话
     */
    void cleanExpiredSessions();

    /**
     * 统计用户的有效会话数量
     *
     * @param userId 用户ID
     * @return 会话数量
     */
    int countActiveSessionsByUserId(Long userId);

    /**
     * 检查会话是否有效
     *
     * @param session 会话信息
     * @return 是否有效
     */
    boolean isSessionValid(SysUserSessionEntity session);

    /**
     * 刷新会话Token
     *
     * @param refreshToken 刷新Token
     * @param newSessionToken 新的会话Token
     * @param newRefreshToken 新的刷新Token
     * @param newExpireTime 新的过期时间
     * @return 是否刷新成功
     */
    boolean refreshSession(String refreshToken, String newSessionToken, String newRefreshToken, LocalDateTime newExpireTime);
}