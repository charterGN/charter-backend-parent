package ink.charter.website.domain.admin.core.repository.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysRoleEntity;
import ink.charter.website.domain.admin.api.dto.role.PageRoleDTO;
import ink.charter.website.domain.admin.api.repository.SysRoleRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统角色领域仓库实现
 *
 * @author charter
 * @create 2025/11/05
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysRoleRepositoryImpl implements SysRoleRepository {

    private final SysRoleMapper sysRoleMapper;

    @Override
    public PageResult<SysRoleEntity> pageRoles(PageRoleDTO pageRequest) {
        IPage<SysRoleEntity> result = sysRoleMapper.pageRoles(pageRequest);
        return PageResult.of(result.getRecords(), result.getTotal());
    }

    @Override
    public SysRoleEntity getById(Long id) {
        if (id == null) {
            return null;
        }
        return sysRoleMapper.selectById(id);
    }

    @Override
    public SysRoleEntity getByRoleCode(String roleCode) {
        return sysRoleMapper.selectByRoleCode(roleCode);
    }

    @Override
    public List<SysRoleEntity> listAllEnabled() {
        return sysRoleMapper.selectAllEnabled();
    }

    @Override
    public List<SysRoleEntity> listByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return sysRoleMapper.selectByUserId(userId);
    }

    @Override
    public boolean create(SysRoleEntity role) {
        if (role == null) {
            return false;
        }
        
        try {
            int result = sysRoleMapper.insert(role);
            return result > 0;
        } catch (Exception e) {
            log.error("创建角色失败", e);
            return false;
        }
    }

    @Override
    public boolean update(SysRoleEntity role) {
        if (role == null || role.getId() == null) {
            return false;
        }
        
        try {
            int result = sysRoleMapper.updateById(role);
            return result > 0;
        } catch (Exception e) {
            log.error("更新角色失败", e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        
        try {
            int result = sysRoleMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return false;
        }
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        try {
            int result = sysRoleMapper.deleteByIds(ids);
            return result > 0;
        } catch (Exception e) {
            log.error("批量删除角色失败", e);
            return false;
        }
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return false;
        }
        
        try {
            int result = sysRoleMapper.updateStatus(id, status);
            return result > 0;
        } catch (Exception e) {
            log.error("更新角色状态失败", e);
            return false;
        }
    }

    @Override
    public boolean existsByRoleCode(String roleCode, Long excludeId) {
        return sysRoleMapper.existsByRoleCode(roleCode, excludeId);
    }
}
