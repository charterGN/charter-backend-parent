package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.entity.sys.SysUserSessionEntity;
import ink.charter.website.domain.admin.api.repository.SysUserSessionRepository;
import ink.charter.website.server.admin.sys.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户会话服务实现类
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    private final SysUserSessionRepository sysUserSessionRepository;

    @Override
    public SysUserSessionEntity createSession(Long userId, String sessionToken, String refreshToken,
                                             String loginIp, String userAgent, LocalDateTime expireTime) {
        return sysUserSessionRepository.createSession(userId, sessionToken, refreshToken, loginIp, userAgent, expireTime);
    }

    @Override
    public SysUserSessionEntity getSessionByToken(String sessionToken) {
        return sysUserSessionRepository.getSessionByToken(sessionToken);
    }

    @Override
    public SysUserSessionEntity getSessionByRefreshToken(String refreshToken) {
        return sysUserSessionRepository.getSessionByRefreshToken(refreshToken);
    }

    @Override
    public List<SysUserSessionEntity> getActiveSessionsByUserId(Long userId) {
        return sysUserSessionRepository.getActiveSessionsByUserId(userId);
    }

    @Override
    public List<SysUserSessionEntity> getSessionsByUserId(Long userId) {
        return sysUserSessionRepository.getSessionsByUserId(userId);
    }

    @Override
    public void invalidateSession(String sessionToken) {
        sysUserSessionRepository.invalidateSession(sessionToken);
    }

    @Override
    public void invalidateUserSessions(Long userId) {
        sysUserSessionRepository.invalidateUserSessions(userId);
    }

    @Override
    public void invalidateOtherUserSessions(Long userId, String currentSessionToken) {
        sysUserSessionRepository.invalidateOtherUserSessions(userId, currentSessionToken);
    }

    @Override
    public void updateExpireTime(String sessionToken, LocalDateTime expireTime) {
        sysUserSessionRepository.updateExpireTime(sessionToken, expireTime);
    }

    @Override
    public void cleanExpiredSessions() {
        sysUserSessionRepository.cleanExpiredSessions();
    }

    @Override
    public int countActiveSessionsByUserId(Long userId) {
        return sysUserSessionRepository.countActiveSessionsByUserId(userId);
    }

    @Override
    public boolean isSessionValid(SysUserSessionEntity session) {
        return sysUserSessionRepository.isSessionValid(session);
    }

    @Override
    public boolean refreshSession(String refreshToken, String newSessionToken, String newRefreshToken, LocalDateTime newExpireTime) {
        return sysUserSessionRepository.refreshSession(refreshToken, newSessionToken, newRefreshToken, newExpireTime);
    }
}