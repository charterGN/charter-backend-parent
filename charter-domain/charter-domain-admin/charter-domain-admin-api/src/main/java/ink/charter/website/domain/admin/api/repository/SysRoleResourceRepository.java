package ink.charter.website.domain.admin.api.repository;

import ink.charter.website.common.core.entity.sys.SysRoleResourceEntity;

import java.util.List;

/**
 * 角色资源关联领域仓库
 *
 * @author charter
 * @create 2025/11/05
 */
public interface SysRoleResourceRepository {

    /**
     * 根据角色ID获取资源ID列表
     *
     * @param roleId 角色ID
     * @return 资源ID列表
     */
    List<Long> getResourceIdsByRoleId(Long roleId);

    /**
     * 根据角色ID删除角色资源关联
     *
     * @param roleId 角色ID
     * @return 是否删除成功
     */
    boolean deleteByRoleId(Long roleId);

    /**
     * 批量保存角色资源关联
     *
     * @param roleResourceList 角色资源关联列表
     * @return 是否保存成功
     */
    boolean saveBatch(List<SysRoleResourceEntity> roleResourceList);

    /**
     * 保存角色资源关联
     *
     * @param roleResource 角色资源关联
     * @return 是否保存成功
     */
    boolean save(SysRoleResourceEntity roleResource);

    /**
     * 根据角色ID和资源ID列表保存关联关系
     *
     * @param roleId 角色ID
     * @param resourceIds 资源ID列表
     * @return 是否保存成功
     */
    boolean saveRoleResources(Long roleId, List<Long> resourceIds);
}
