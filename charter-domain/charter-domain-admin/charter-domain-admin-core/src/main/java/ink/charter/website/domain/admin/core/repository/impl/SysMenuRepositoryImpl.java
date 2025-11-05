package ink.charter.website.domain.admin.core.repository.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.domain.admin.api.dto.menu.PageMenuDTO;
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
    public PageResult<SysMenuEntity> pageMenus(PageMenuDTO pageRequest) {
        IPage<SysMenuEntity> result = sysMenuMapper.pageResources(pageRequest);
        return PageResult.of(result.getRecords(), result.getTotal());
    }

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
    public List<SysMenuEntity> listAll() {
        return sysMenuMapper.selectAll();
    }

    @Override
    public SysMenuEntity getById(Long menuId) {
        if (menuId == null) {
            return null;
        }
        return sysMenuMapper.selectById(menuId);
    }

    @Override
    public List<SysMenuEntity> listByParentId(Long parentId) {
        if (parentId == null) {
            return List.of();
        }
        return sysMenuMapper.selectByParentId(parentId);
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

    @Override
    public boolean batchDelete(List<Long> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            return false;
        }
        try {
            return sysMenuMapper.deleteByIds(menuIds) > 0;
        } catch (Exception e) {
            log.error("批量删除菜单失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateStatus(Long menuId, Integer status) {
        if (menuId == null || status == null) {
            return false;
        }
        try {
            return sysMenuMapper.updateStatus(menuId, status) > 0;
        } catch (Exception e) {
            log.error("更新菜单状态失败: {}", e.getMessage(), e);
            return false;
        }
    }
}