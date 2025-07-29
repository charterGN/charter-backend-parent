package ink.charter.website.server.admin.sys.service;

import ink.charter.website.server.admin.sys.vo.menu.DynamicMenuVO;
import ink.charter.website.server.admin.sys.vo.menu.MenuVO;

import java.util.List;

/**
 * 菜单服务接口
 *
 * @author charter
 * @create 2025/07/28
 */
public interface MenuService {

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
     * 根据角色ID获取菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> listMenuIdsByRoleId(Long roleId);

    /**
     * 保存角色菜单关联
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    void saveRoleMenu(Long roleId, List<Long> menuIds);
}