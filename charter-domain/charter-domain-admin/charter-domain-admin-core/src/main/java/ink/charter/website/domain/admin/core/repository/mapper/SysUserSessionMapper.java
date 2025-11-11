package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.common.PageRequest;
import ink.charter.website.common.core.entity.sys.SysUserSessionEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.admin.api.dto.session.PageUserSessionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统用户会话Mapper
 *
 * @author charter
 * @create 2025/07/17
 */
@Mapper
public interface SysUserSessionMapper extends BaseMapper<SysUserSessionEntity> {

    /**
     * 分页查询用户会话
     *
     * @param pageRequest 分页查询参数
     * @return 分页结果
     */
    default IPage<SysUserSessionEntity> pageSessions(PageUserSessionDTO pageRequest) {
        PageRequest page = pageRequest.getPageRequest();
        return selectPage(new Page<>(page.getPageNo(), page.getPageSize()), QueryWrappers.<SysUserSessionEntity>lambdaQuery()
                .eqIfPresent(SysUserSessionEntity::getUserId, pageRequest.getUserId())
                .likeIfPresent(SysUserSessionEntity::getLoginIp, pageRequest.getLoginIp())
                .eqIfPresent(SysUserSessionEntity::getStatus, pageRequest.getStatus())
                .geIfPresent(SysUserSessionEntity::getLoginTime, pageRequest.getLoginStartTime())
                .leIfPresent(SysUserSessionEntity::getLoginTime, pageRequest.getLoginEndTime())
                .orderByDesc(SysUserSessionEntity::getLoginTime));
    }

    /**
     * 根据会话Token查询会话信息
     *
     * @param sessionToken 会话Token
     * @return 会话信息
     */
    default SysUserSessionEntity selectBySessionToken(String sessionToken) {
        return selectOne(QueryWrappers.<SysUserSessionEntity>lambdaQuery()
            .eqIfPresent(SysUserSessionEntity::getToken, sessionToken)
            .eq(SysUserSessionEntity::getStatus, 1));
    }

    /**
     * 根据刷新Token查询会话信息
     *
     * @param refreshToken 刷新Token
     * @return 会话信息
     */
    default SysUserSessionEntity selectByRefreshToken(String refreshToken) {
        return selectOne(QueryWrappers.<SysUserSessionEntity>lambdaQuery()
            .eqIfPresent(SysUserSessionEntity::getRefreshToken, refreshToken)
            .eq(SysUserSessionEntity::getStatus, 1));
    }

    /**
     * 查询用户的有效会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    default List<SysUserSessionEntity> selectActiveSessionsByUserId(Long userId) {
        return selectList(QueryWrappers.<SysUserSessionEntity>lambdaQuery()
            .eqIfPresent(SysUserSessionEntity::getUserId, userId)
            .eq(SysUserSessionEntity::getStatus, 1)
            .gt(SysUserSessionEntity::getExpireTime, LocalDateTime.now())
            .eq(SysUserSessionEntity::getIsDeleted, 0)
            .orderByDesc(SysUserSessionEntity::getLoginTime));
    }

    /**
     * 查询用户的所有会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    default List<SysUserSessionEntity> selectSessionsByUserId(Long userId) {
        return selectList(QueryWrappers.<SysUserSessionEntity>lambdaQuery()
            .eqIfPresent(SysUserSessionEntity::getUserId, userId)
            .eq(SysUserSessionEntity::getIsDeleted, 0)
            .orderByDesc(SysUserSessionEntity::getLoginTime));
    }

    /**
     * 使会话失效
     *
     * @param sessionToken 会话Token
     */
    default void invalidateSession(String sessionToken) {
        update(null, QueryWrappers.<SysUserSessionEntity>lambdaUpdate()
            .set(SysUserSessionEntity::getStatus, 0)
            .eqIfPresent(SysUserSessionEntity::getToken, sessionToken));
    }

    /**
     * 使用户的所有会话失效
     *
     * @param userId 用户ID
     */
    default void invalidateUserSessions(Long userId) {
        update(null, QueryWrappers.<SysUserSessionEntity>lambdaUpdate()
            .set(SysUserSessionEntity::getStatus, 0)
            .eqIfPresent(SysUserSessionEntity::getUserId, userId)
            .eq(SysUserSessionEntity::getStatus, 1));
    }

    /**
     * 使用户的其他会话失效（除了当前会话）
     *
     * @param userId 用户ID
     * @param currentSessionToken 当前会话Token
     */
    default void invalidateOtherUserSessions(Long userId, String currentSessionToken) {
        update(null, QueryWrappers.<SysUserSessionEntity>lambdaUpdate()
            .set(SysUserSessionEntity::getStatus, 0)
            .eqIfPresent(SysUserSessionEntity::getUserId, userId)
            .neIfPresent(SysUserSessionEntity::getToken, currentSessionToken)
            .eq(SysUserSessionEntity::getStatus, 1));
    }

    /**
     * 更新会话过期时间
     *
     * @param sessionToken 会话Token
     * @param expireTime 过期时间
     */
    default void updateExpireTime(String sessionToken, LocalDateTime expireTime) {
        update(null, QueryWrappers.<SysUserSessionEntity>lambdaUpdate()
            .set(SysUserSessionEntity::getExpireTime, expireTime)
            .eqIfPresent(SysUserSessionEntity::getToken, sessionToken));
    }

    /**
     * 清理过期会话
     */
    default void cleanExpiredSessions() {
        update(null, QueryWrappers.<SysUserSessionEntity>lambdaUpdate()
            .set(SysUserSessionEntity::getStatus, 0)
            .lt(SysUserSessionEntity::getExpireTime, LocalDateTime.now())
            .eq(SysUserSessionEntity::getStatus, 1)
            .eq(SysUserSessionEntity::getIsDeleted, 0));
    }

    /**
     * 统计用户的有效会话数量
     *
     * @param userId 用户ID
     * @return 会话数量
     */
    default int countActiveSessionsByUserId(Long userId) {
        return Math.toIntExact(selectCount(QueryWrappers.<SysUserSessionEntity>lambdaQuery()
            .eqIfPresent(SysUserSessionEntity::getUserId, userId)
            .eq(SysUserSessionEntity::getStatus, 1)
            .gt(SysUserSessionEntity::getExpireTime, LocalDateTime.now())
            .eq(SysUserSessionEntity::getIsDeleted, 0)));
    }
}