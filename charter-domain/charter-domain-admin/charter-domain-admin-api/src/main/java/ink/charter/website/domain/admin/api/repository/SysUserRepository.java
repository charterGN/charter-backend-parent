package ink.charter.website.domain.admin.api.repository;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysUserEntity;
import ink.charter.website.domain.admin.api.dto.user.PageUserDTO;

import java.util.List;
import java.util.Set;

/**
 * 系统用户领域仓库
 * @author charter
 * @create 2025/07/23
 */
public interface SysUserRepository {

    /**
     * 分页查询用户
     * @param pageRequest 分页参数
     * @return 用户页面
     */
    PageResult<SysUserEntity> pageUsers(PageUserDTO pageRequest);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUserEntity getUserByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    SysUserEntity getUserByEmail(String email);

    /**
     * 根据用户名或邮箱查询用户
     *
     * @param usernameOrEmail 用户名或邮箱
     * @return 用户信息
     */
    SysUserEntity getUserByUsernameOrEmail(String usernameOrEmail);

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    SysUserEntity getUserById(Long userId);

    /**
     * 查询用户的权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> getUserPermissions(Long userId);

    /**
     * 查询用户的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    Set<String> getUserRoles(Long userId);

    /**
     * 根据用户ID获取角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     */
    void updateLoginInfo(Long userId, String loginIp);

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 是否创建成功
     */
    boolean createUser(SysUserEntity user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 是否更新成功
     */
    boolean updateUser(SysUserEntity user);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(Long userId);

    /**
     * 批量删除用户
     *
     * @param userIds 用户ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteUsers(List<Long> userIds);

    /**
     * 根据用户ID列表批量查询用户
     *
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    List<SysUserEntity> getUsersByIds(List<Long> userIds);
}
