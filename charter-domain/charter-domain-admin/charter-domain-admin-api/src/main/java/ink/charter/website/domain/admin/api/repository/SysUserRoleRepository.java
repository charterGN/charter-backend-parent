package ink.charter.website.domain.admin.api.repository;

import ink.charter.website.common.core.entity.sys.SysUserRoleEntity;

import java.util.List;

/**
 * 用户角色关联领域仓库
 *
 * @author charter
 * @create 2025/11/05
 */
public interface SysUserRoleRepository {

    /**
     * 根据用户ID获取角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 根据用户ID删除用户角色关联
     *
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteByUserId(Long userId);

    /**
     * 批量保存用户角色关联
     *
     * @param userRoleList 用户角色关联列表
     * @return 是否保存成功
     */
    boolean saveBatch(List<SysUserRoleEntity> userRoleList);

    /**
     * 保存用户角色关联
     *
     * @param userRole 用户角色关联
     * @return 是否保存成功
     */
    boolean save(SysUserRoleEntity userRole);

    /**
     * 根据用户ID和角色ID列表保存关联关系
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否保存成功
     */
    boolean saveUserRoles(Long userId, List<Long> roleIds);
}
