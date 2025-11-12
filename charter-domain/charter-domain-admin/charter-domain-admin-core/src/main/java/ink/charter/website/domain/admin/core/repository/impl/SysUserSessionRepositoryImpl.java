package ink.charter.website.domain.admin.core.repository.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysUserSessionEntity;
import ink.charter.website.common.core.utils.IpUtils;
import ink.charter.website.domain.admin.api.dto.session.PageUserSessionDTO;
import ink.charter.website.domain.admin.api.repository.SysUserSessionRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysUserSessionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统用户会话领域仓库实现
 * @author charter
 * @create 2025/07/23
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysUserSessionRepositoryImpl implements SysUserSessionRepository {

    private final SysUserSessionMapper sysUserSessionMapper;

    @Override
    public PageResult<SysUserSessionEntity> pageSessions(PageUserSessionDTO pageRequest) {
        try {
            Page<SysUserSessionEntity> page = new Page<>(
                pageRequest.getPageRequest().getPageNo(),
                pageRequest.getPageRequest().getPageSize()
            );
            
            IPage<SysUserSessionEntity> result = sysUserSessionMapper.pageSessionsWithUsername(
                page,
                pageRequest.getUsername(),
                pageRequest.getLoginIp(),
                pageRequest.getStatus(),
                pageRequest.getLoginStartTime(),
                pageRequest.getLoginEndTime()
            );
            
            return PageResult.of(result.getRecords(), result.getTotal());
        } catch (Exception e) {
            log.error("分页查询用户会话失败: {}", e.getMessage(), e);
            return PageResult.empty();
        }
    }

    @Override
    public SysUserSessionEntity createSession(Long userId, String sessionToken, String refreshToken,
                                             String loginIp, String userAgent, LocalDateTime expireTime) {
        if (userId == null || !StringUtils.hasText(sessionToken)) {
            return null;
        }

        try {
            SysUserSessionEntity session = new SysUserSessionEntity();
            session.setUserId(userId);
            session.setSessionId(java.util.UUID.randomUUID().toString());
            session.setToken(sessionToken);
            session.setRefreshToken(refreshToken);
            session.setLoginIp(loginIp);
            session.setLoginAddress(IpUtils.getIpLocation(loginIp));
            session.setUserAgent(userAgent);
            session.setLoginTime(LocalDateTime.now());
            session.setExpireTime(expireTime);
            session.setStatus(1); // 1-有效

            int result = sysUserSessionMapper.insert(session);
            return result > 0 ? session : null;
        } catch (Exception e) {
            log.error("创建用户会话失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public SysUserSessionEntity getSessionByToken(String sessionToken) {
        return sysUserSessionMapper.selectBySessionToken(sessionToken);
    }

    @Override
    public SysUserSessionEntity getSessionByRefreshToken(String refreshToken) {
        return sysUserSessionMapper.selectByRefreshToken(refreshToken);
    }

    @Override
    public List<SysUserSessionEntity> getActiveSessionsByUserId(Long userId) {
        return sysUserSessionMapper.selectActiveSessionsByUserId(userId);
    }

    @Override
    public List<SysUserSessionEntity> getSessionsByUserId(Long userId) {
        return sysUserSessionMapper.selectSessionsByUserId(userId);
    }

    @Override
    public void invalidateSession(String sessionToken) {
        if (StringUtils.hasText(sessionToken)) {
            sysUserSessionMapper.invalidateSession(sessionToken);
        }
    }

    @Override
    public void invalidateUserSessions(Long userId) {
        if (userId != null) {
            sysUserSessionMapper.invalidateUserSessions(userId);
        }
    }

    @Override
    public void invalidateOtherUserSessions(Long userId, String currentSessionToken) {
        if (userId != null && StringUtils.hasText(currentSessionToken)) {
            sysUserSessionMapper.invalidateOtherUserSessions(userId, currentSessionToken);
        }
    }

    @Override
    public void updateExpireTime(String sessionToken, LocalDateTime expireTime) {
        if (StringUtils.hasText(sessionToken) && expireTime != null) {
            sysUserSessionMapper.updateExpireTime(sessionToken, expireTime);
        }
    }

    @Override
    public void cleanExpiredSessions() {
        try {
            sysUserSessionMapper.cleanExpiredSessions();
            log.debug("清理过期会话完成");
        } catch (Exception e) {
            log.error("清理过期会话失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public int countActiveSessionsByUserId(Long userId) {
        return sysUserSessionMapper.countActiveSessionsByUserId(userId);
    }

    @Override
    public boolean isSessionValid(SysUserSessionEntity session) {
        if (session == null) {
            return false;
        }

        // 检查会话状态
        if (!Integer.valueOf(1).equals(session.getStatus())) {
            return false;
        }

        // 检查是否过期
        return session.getExpireTime() == null || !session.getExpireTime().isBefore(LocalDateTime.now());
    }

    @Override
    public boolean refreshSession(String refreshToken, String newSessionToken, String newRefreshToken, LocalDateTime newExpireTime) {
        if (!StringUtils.hasText(refreshToken) || !StringUtils.hasText(newSessionToken)) {
            return false;
        }

        try {
            // 查询原会话
            SysUserSessionEntity session = getSessionByRefreshToken(refreshToken);
            if (session == null || !isSessionValid(session)) {
                return false;
            }

            // 更新会话信息
            session.setToken(newSessionToken);
            session.setRefreshToken(newRefreshToken);
            session.setExpireTime(newExpireTime);

            return sysUserSessionMapper.updateById(session) > 0;
        } catch (Exception e) {
            log.error("刷新会话失败: {}", e.getMessage(), e);
            return false;
        }
    }
}
