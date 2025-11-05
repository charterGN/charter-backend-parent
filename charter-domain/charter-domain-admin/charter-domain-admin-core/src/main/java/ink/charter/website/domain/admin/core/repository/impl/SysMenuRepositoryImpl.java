package ink.charter.website.domain.admin.core.repository.impl;


import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.domain.admin.api.repository.SysMenuRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysMenuMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统菜单领域仓库实现
 * @author charter
 * @create 2025/07/28
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysMenuRepositoryImpl implements SysMenuRepository {

    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenuEntity> listByIds(List<Long> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            return List.of();
        }
        return sysMenuMapper.selectByIds(menuIds);
    }

    @Override
    public List<SysMenuEntity> listNormalMenus() {
        return sysMenuMapper.selectNormalMenus();
    }

    @Override
    public SysMenuEntity getById(Long menuId) {
        if (menuId == null) {
            return null;
        }
        return sysMenuMapper.selectById(menuId);
    }

    @Override
    public boolean save(SysMenuEntity menu) {
        if (menu == null) {
            return false;
        }
        try {
            return sysMenuMapper.insert(menu) > 0;
        } catch (Exception e) {
            log.error("保存菜单失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean update(SysMenuEntity menu) {
        if (menu == null || menu.getId() == null) {
            return false;
        }
        try {
            return sysMenuMapper.updateById(menu) > 0;
        } catch (Exception e) {
            log.error("更新菜单失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean delete(Long menuId) {
        if (menuId == null) {
            return false;
        }
        try {
            return sysMenuMapper.deleteById(menuId) > 0;
        } catch (Exception e) {
            log.error("删除菜单失败: {}", e.getMessage(), e);
            return false;
        }
    }
}