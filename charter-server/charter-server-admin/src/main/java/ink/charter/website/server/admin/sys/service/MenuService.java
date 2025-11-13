package ink.charter.website.server.admin.sys.service;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.domain.admin.api.dto.menu.PageMenuDTO;
import ink.charter.website.domain.admin.api.vo.menu.DynamicMenuVO;
import ink.charter.website.domain.admin.api.vo.menu.MenuVO;

import java.util.List;

/**
 * 菜单服务接口
 *
 * @author charter
 * @create 2025/07/28
 */
public interface MenuService {

    /**
     * 条件查询菜单
     *
     * @param pageRequest 查询参数
     * @return 结果
     */
    List<MenuVO> pageMenus(PageMenuDTO pageRequest);

    /**
     * 获取用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<DynamicMenuVO> listRouters(Long userId);

    /**
     * 获取所有正常菜单列表
     *
     * @return 菜单列表
     */
    List<MenuVO> listMenuNormal();

    /**
     * 查询所有菜单列表
     *
     * @return 菜单列表
     */
    List<MenuVO> listAll();

    /**
     * 根据ID查询菜单
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    MenuVO getById(Long menuId);

    /**
     * 根据父菜单ID查询子菜单列表
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<MenuVO> listByParentId(Long parentId);

    /**
     * 根据角色ID获取菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> listMenuIdsByRoleId(Long roleId);

    /**
     * 创建菜单
     *
     * @param menu 菜单信息
     * @return 是否创建成功
     */
    boolean create(SysMenuEntity menu);

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

    /**
     * 批量删除菜单
     *
     * @param menuIds 菜单ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> menuIds);

    /**
     * 更新菜单状态
     *
     * @param menuId 菜单ID
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long menuId, Integer status);

    /**
     * 保存角色菜单关联
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    void saveRoleMenu(Long roleId, List<Long> menuIds);
}