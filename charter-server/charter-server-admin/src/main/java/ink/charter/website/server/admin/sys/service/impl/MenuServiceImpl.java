package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.domain.admin.api.repository.SysMenuRepository;
import ink.charter.website.domain.admin.api.repository.SysRoleMenuRepository;
import ink.charter.website.server.admin.sys.converter.MenuConverter;
import ink.charter.website.server.admin.sys.service.MenuService;
import ink.charter.website.server.admin.sys.service.UserService;
import ink.charter.website.server.admin.sys.vo.menu.DynamicMenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 *
 * @author charter
 * @create 2025/07/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final SysMenuRepository sysMenuRepository;
    private final SysRoleMenuRepository sysRoleMenuRepository;
    private final UserService userService;
    private final MenuConverter menuConverter;

    /**
     * 获取用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<DynamicMenuVO> listRouters(Long userId) {
        // 获取用户角色列表
        List<Long> roleIds = userService.getRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取角色菜单列表
        List<Long> menuIds = new ArrayList<>();
        for (Long roleId : roleIds) {
            List<Long> roleMenuIds = listMenuIdsByRoleId(roleId);
            if (roleMenuIds != null && !roleMenuIds.isEmpty()) {
                menuIds.addAll(roleMenuIds);
            }
        }

        // 去重
        menuIds = menuIds.stream().distinct().collect(Collectors.toList());

        if (menuIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 查询菜单列表
        List<SysMenuEntity> menuList = sysMenuRepository.listByIds(menuIds);
        // 过滤出状态正常的菜单
        menuList = menuList.stream()
                .filter(menu -> menu.getStatus() == 1)
                .collect(Collectors.toList());

        // 转换为前端需要的格式
        return menuConverter.convertToVOList(menuList);
    }

    /**
     * 获取所有正常菜单列表
     *
     * @return 菜单列表
     */
    @Override
    public List<SysMenuEntity> listMenuNormal() {
        return sysMenuRepository.listNormalMenus();
    }

    /**
     * 根据角色ID获取菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    @Override
    public List<Long> listMenuIdsByRoleId(Long roleId) {
        return sysRoleMenuRepository.getMenuIdsByRoleId(roleId);
    }

    /**
     * 保存角色菜单关联
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenu(Long roleId, List<Long> menuIds) {
        sysRoleMenuRepository.saveRoleMenus(roleId, menuIds);
    }
}