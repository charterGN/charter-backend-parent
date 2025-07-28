package ink.charter.website.domain.admin.api.repository;

import ink.charter.website.common.core.entity.sys.SysRoleMenuEntity;

import java.util.List;

/**
 * 系统角色菜单关联领域仓库
 * @author charter
 * @create 2025/07/28
 */
public interface SysRoleMenuRepository {

    /**
     * 根据角色ID获取菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 根据角色ID删除角色菜单关联
     *
     * @param roleId 角色ID
     * @return 是否删除成功
     */
    boolean deleteByRoleId(Long roleId);

    /**
     * 批量保存角色菜单关联
     *
     * @param roleMenuList 角色菜单关联列表
     * @return 是否保存成功
     */
    boolean saveBatch(List<SysRoleMenuEntity> roleMenuList);

    /**
     * 保存角色菜单关联
     *
     * @param roleMenu 角色菜单关联
     * @return 是否保存成功
     */
    boolean save(SysRoleMenuEntity roleMenu);

    /**
     * 根据角色ID和菜单ID列表保存关联关系
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 是否保存成功
     */
    boolean saveRoleMenus(Long roleId, List<Long> menuIds);
}