package ink.charter.website.domain.admin.core.repository.impl;


import ink.charter.website.common.core.entity.sys.SysRoleMenuEntity;
import ink.charter.website.domain.admin.api.repository.SysRoleMenuRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysRoleMenuMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统角色菜单关联领域仓库实现
 * @author charter
 * @create 2025/07/28
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysRoleMenuRepositoryImpl implements SysRoleMenuRepository {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        if (roleId == null) {
            return List.of();
        }
        List<SysRoleMenuEntity> roleMenuList = sysRoleMenuMapper.selectByRoleId(roleId);
        return roleMenuList.stream()
                .map(SysRoleMenuEntity::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteByRoleId(Long roleId) {
        if (roleId == null) {
            return false;
        }
        try {
            return sysRoleMenuMapper.deleteByRoleId(roleId) >= 0;
        } catch (Exception e) {
            log.error("删除角色菜单关联失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean saveBatch(List<SysRoleMenuEntity> roleMenuList) {
        if (roleMenuList == null || roleMenuList.isEmpty()) {
            return true;
        }
        try {
            for (SysRoleMenuEntity roleMenu : roleMenuList) {
                sysRoleMenuMapper.insert(roleMenu);
            }
            return true;
        } catch (Exception e) {
            log.error("批量保存角色菜单关联失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean save(SysRoleMenuEntity roleMenu) {
        if (roleMenu == null) {
            return false;
        }
        try {
            return sysRoleMenuMapper.insert(roleMenu) > 0;
        } catch (Exception e) {
            log.error("保存角色菜单关联失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRoleMenus(Long roleId, List<Long> menuIds) {
        if (roleId == null) {
            return false;
        }
        
        try {
            // 删除原有关联
            deleteByRoleId(roleId);
            
            // 添加新关联
            if (menuIds != null && !menuIds.isEmpty()) {
                List<SysRoleMenuEntity> roleMenuList = new ArrayList<>();
                for (Long menuId : menuIds) {
                    SysRoleMenuEntity roleMenu = new SysRoleMenuEntity();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menuId);
                    roleMenuList.add(roleMenu);
                }
                return saveBatch(roleMenuList);
            }
            return true;
        } catch (Exception e) {
            log.error("保存角色菜单关联失败: {}", e.getMessage(), e);
            return false;
        }
    }
}