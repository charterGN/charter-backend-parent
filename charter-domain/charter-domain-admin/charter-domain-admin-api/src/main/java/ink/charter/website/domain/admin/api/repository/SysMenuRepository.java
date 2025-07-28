package ink.charter.website.domain.admin.api.repository;

import ink.charter.website.common.core.entity.sys.SysMenuEntity;

import java.util.List;

/**
 * 系统菜单领域仓库
 * @author charter
 * @create 2025/07/28
 */
public interface SysMenuRepository {

    /**
     * 根据ID列表批量查询菜单
     *
     * @param menuIds 菜单ID列表
     * @return 菜单列表
     */
    List<SysMenuEntity> listByIds(List<Long> menuIds);

    /**
     * 获取所有正常状态的菜单列表
     *
     * @return 菜单列表
     */
    List<SysMenuEntity> listNormalMenus();

    /**
     * 根据ID查询菜单
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SysMenuEntity getById(Long menuId);

    /**
     * 保存菜单
     *
     * @param menu 菜单信息
     * @return 是否保存成功
     */
    boolean save(SysMenuEntity menu);

    /**
     * 更新菜单
     *
     * @param menu 菜单信息
     * @return 是否更新成功
     */
    boolean update(SysMenuEntity menu);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return 是否删除成功
     */
    boolean delete(Long menuId);
}