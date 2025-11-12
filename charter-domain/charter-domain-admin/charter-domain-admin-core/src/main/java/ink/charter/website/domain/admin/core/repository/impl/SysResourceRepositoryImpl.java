package ink.charter.website.domain.admin.core.repository.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.common.PageRequest;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysResourceEntity;
import ink.charter.website.domain.admin.api.dto.resource.PageResourceDTO;
import ink.charter.website.domain.admin.api.repository.SysResourceRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysResourceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统资源领域仓库实现
 *
 * @author charter
 * @create 2025/11/05
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysResourceRepositoryImpl implements SysResourceRepository {

    private final SysResourceMapper sysResourceMapper;

    @Override
    public PageResult<SysResourceEntity> pageResources(PageResourceDTO pageRequest) {
        IPage<SysResourceEntity> result = sysResourceMapper.pageResources(pageRequest);
        return PageResult.of(result.getRecords(), result.getTotal());
    }

    @Override
    public SysResourceEntity getById(Long id) {
        if (id == null) {
            return null;
        }
        return sysResourceMapper.selectById(id);
    }

    @Override
    public SysResourceEntity getByResourceCode(String resourceCode) {
        return sysResourceMapper.selectByResourceCode(resourceCode);
    }

    @Override
    public List<SysResourceEntity> listByModule(String module) {
        return sysResourceMapper.selectByModule(module);
    }

    @Override
    public List<String> listAllModules() {
        return sysResourceMapper.selectAllModules();
    }

    @Override
    public List<SysResourceEntity> listAllEnabled() {
        return sysResourceMapper.selectAllEnabled();
    }

    @Override
    public List<SysResourceEntity> listByIds(List<Long> resourceIds) {
        if (resourceIds == null || resourceIds.isEmpty()) {
            return List.of();
        }
        return sysResourceMapper.selectByIds(resourceIds);
    }

    @Override
    public boolean create(SysResourceEntity resource) {
        if (resource == null) {
            return false;
        }
        
        try {
            int result = sysResourceMapper.insert(resource);
            return result > 0;
        } catch (Exception e) {
            log.error("创建资源失败", e);
            return false;
        }
    }

    @Override
    public boolean update(SysResourceEntity resource) {
        if (resource == null || resource.getId() == null) {
            return false;
        }
        
        try {
            int result = sysResourceMapper.updateById(resource);
            return result > 0;
        } catch (Exception e) {
            log.error("更新资源失败", e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        
        try {
            int result = sysResourceMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除资源失败", e);
            return false;
        }
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        try {
            int result = sysResourceMapper.deleteByIds(ids);
            return result > 0;
        } catch (Exception e) {
            log.error("批量删除资源失败", e);
            return false;
        }
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return false;
        }
        
        try {
            int result = sysResourceMapper.updateStatus(id, status);
            return result > 0;
        } catch (Exception e) {
            log.error("更新资源状态失败", e);
            return false;
        }
    }

    @Override
    public boolean existsByResourceCode(String resourceCode, Long excludeId) {
        return sysResourceMapper.existsByResourceCode(resourceCode, excludeId);
    }
}
