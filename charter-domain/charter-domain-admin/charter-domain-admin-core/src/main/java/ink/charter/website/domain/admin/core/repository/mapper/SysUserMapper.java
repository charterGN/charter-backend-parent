package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.core.entity.sys.SysUserEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * 系统用户Mapper
 *
 * @author charter
 * @create 2025/07/17
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    default SysUserEntity selectByUsername(String username) {
        return selectOne(QueryWrappers.<SysUserEntity>lambdaQuery()
            .eqIfPresent(SysUserEntity::getUsername, username));
    }

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    default SysUserEntity selectByEmail(String email) {
        return selectOne(QueryWrappers.<SysUserEntity>lambdaQuery()
            .eqIfPresent(SysUserEntity::getEmail, email));
    }

    /**
     * 根据用户名或邮箱查询用户
     *
     * @param usernameOrEmail 用户名或邮箱
     * @return 用户信息
     */
    default SysUserEntity selectByUsernameOrEmail(String usernameOrEmail) {
        return selectOne(QueryWrappers.<SysUserEntity>lambdaQuery()
            .and(wrapper -> wrapper
                .eq(SysUserEntity::getUsername, usernameOrEmail)
                .or()
                .eq(SysUserEntity::getEmail, usernameOrEmail)
            ));
    }

    /**
     * 查询用户的权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("""
            SELECT DISTINCT sr.resource_code
            FROM sys_user_role sur
            LEFT JOIN sys_role_resource srr ON sur.role_id = srr.role_id
            LEFT JOIN sys_resource sr ON srr.resource_id = sr.id
            WHERE sur.user_id = #{userId} 
            AND sur.is_deleted = 0 
            AND srr.is_deleted = 0 
            AND sr.is_deleted = 0 
            AND sr.status = 1
            """)
    Set<String> selectUserPermissions(@Param("userId") Long userId);

    /**
     * 查询用户的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Select("""
            SELECT DISTINCT sr.role_code
            FROM sys_user_role sur
            LEFT JOIN sys_role sr ON sur.role_id = sr.id
            WHERE sur.user_id = #{userId} 
            AND sur.is_deleted = 0 
            AND sr.is_deleted = 0 
            AND sr.status = 1
            """)
    Set<String> selectUserRoles(@Param("userId") Long userId);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     */
    default void updateLoginInfo(Long userId, String loginIp) {
        update(null, QueryWrappers.<SysUserEntity>lambdaUpdate()
            .setSql("login_count = login_count + 1")
            .set(SysUserEntity::getLastLoginTime, java.time.LocalDateTime.now())
            .set(SysUserEntity::getLastLoginIp, loginIp)
            .eq(SysUserEntity::getId, userId));
    }
}