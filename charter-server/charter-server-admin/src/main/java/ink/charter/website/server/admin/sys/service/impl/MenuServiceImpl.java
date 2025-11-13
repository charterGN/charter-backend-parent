package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.common.core.exception.BusinessException;
import ink.charter.website.common.core.utils.IdGenerator;
import ink.charter.website.domain.admin.api.dto.menu.PageMenuDTO;
import ink.charter.website.domain.admin.api.repository.SysMenuRepository;
import ink.charter.website.domain.admin.api.repository.SysRoleMenuRepository;
import ink.charter.website.server.admin.sys.converter.MenuConverter;
import ink.charter.website.server.admin.sys.service.MenuService;
import ink.charter.website.server.admin.sys.service.UserService;
import ink.charter.website.domain.admin.api.vo.menu.DynamicMenuVO;
import ink.charter.website.domain.admin.api.vo.menu.MenuVO;
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

    @Override
    public List<MenuVO> pageMenus(PageMenuDTO pageRequest) {
        List<SysMenuEntity> pageResult = sysMenuRepository.pageMenus(pageRequest);
        return menuConverter.convertToMenuVOList(pageResult);
    }

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
    public List<MenuVO> listMenuNormal() {
        List<SysMenuEntity> menuList = sysMenuRepository.listNormalMenus();
        return menuConverter.convertToMenuVOList(menuList);
    }

    @Override
    public List<MenuVO> listAll() {
        List<SysMenuEntity> menuList = sysMenuRepository.listAll();
        return menuConverter.convertToMenuVOList(menuList);
    }

    @Override
    public MenuVO getById(Long menuId) {
        if (menuId == null) {
            throw BusinessException.of("菜单ID不能为空");
        }
        SysMenuEntity menu = sysMenuRepository.getById(menuId);
        if (menu == null) {
            throw BusinessException.dataNotFound("菜单");
        }
        return menuConverter.convertToMenuVO(menu);
    }

    @Override
    public List<MenuVO> listByParentId(Long parentId) {
        if (parentId == null) {
            return new ArrayList<>();
        }
        List<SysMenuEntity> menuList = sysMenuRepository.listByParentId(parentId);
        return menuConverter.convertToMenuVOList(menuList);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(SysMenuEntity menu) {
        if (menu == null) {
            throw BusinessException.of("菜单信息不能为空");
        }
        
        // 生成ID
        menu.setId(IdGenerator.snowflakeId());
        
        // 设置默认值
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getVisible() == null) {
            menu.setVisible(1);
        }
        if (menu.getCache() == null) {
            menu.setCache(0);
        }
        if (menu.getExternalLink() == null) {
            menu.setExternalLink(0);
        }
        if (menu.getSortOrder() == null) {
            menu.setSortOrder(0);
        }
        
        boolean result = sysMenuRepository.save(menu);
        if (!result) {
            throw BusinessException.of("创建菜单失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SysMenuEntity menu) {
        if (menu == null || menu.getId() == null) {
            throw BusinessException.of("菜单信息不完整");
        }
        
        // 检查菜单是否存在
        SysMenuEntity existMenu = sysMenuRepository.getById(menu.getId());
        if (existMenu == null) {
            throw BusinessException.dataNotFound("菜单");
        }
        
        // 不允许将父菜单设置为自己
        if (menu.getParentId() != null && menu.getParentId().equals(menu.getId())) {
            throw BusinessException.of("父菜单不能是自己");
        }
        
        boolean result = sysMenuRepository.update(menu);
        if (!result) {
            throw BusinessException.of("更新菜单失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long menuId) {
        if (menuId == null) {
            throw BusinessException.of("菜单ID不能为空");
        }
        
        // 检查是否有子菜单
        List<SysMenuEntity> children = sysMenuRepository.listByParentId(menuId);
        if (children != null && !children.isEmpty()) {
            throw BusinessException.of("存在子菜单，无法删除");
        }
        
        boolean result = sysMenuRepository.delete(menuId);
        if (!result) {
            throw BusinessException.of("删除菜单失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            throw BusinessException.of("菜单ID列表不能为空");
        }
        
        // 检查每个菜单是否有子菜单
        for (Long menuId : menuIds) {
            List<SysMenuEntity> children = sysMenuRepository.listByParentId(menuId);
            if (children != null && !children.isEmpty()) {
                throw BusinessException.of("存在子菜单，无法删除");
            }
        }
        
        boolean result = sysMenuRepository.batchDelete(menuIds);
        if (!result) {
            throw BusinessException.of("批量删除菜单失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long menuId, Integer status) {
        if (menuId == null) {
            throw BusinessException.of("菜单ID不能为空");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw BusinessException.of("状态值不合法");
        }
        
        boolean result = sysMenuRepository.updateStatus(menuId, status);
        if (!result) {
            throw BusinessException.of("更新菜单状态失败");
        }
        return true;
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