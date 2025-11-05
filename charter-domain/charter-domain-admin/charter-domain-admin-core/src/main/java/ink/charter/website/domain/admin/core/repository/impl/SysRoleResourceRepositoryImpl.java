package ink.charter.website.domain.admin.core.repository.impl;

import ink.charter.website.common.core.entity.sys.SysRoleResourceEntity;
import ink.charter.website.common.core.utils.IdGenerator;
import ink.charter.website.domain.admin.api.repository.SysRoleResourceRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysRoleResourceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色资源关联领域仓库实现
 *
 * @author charter
 * @create 2025/11/05
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysRoleResourceRepositoryImpl implements SysRoleResourceRepository {

    private final SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    public List<Long> getResourceIdsByRoleId(Long roleId) {
        if (roleId == null) {
            return List.of();
        }
        
        List<SysRoleResourceEntity> roleResourceList = sysRoleResourceMapper.selectByRoleId(roleId);
        return roleResourceList.stream()
                .map(SysRoleResourceEntity::getResourceId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteByRoleId(Long roleId) {
        if (roleId == null) {
            return false;
        }
        
        try {
            sysRoleResourceMapper.deleteByRoleId(roleId);
            return true;
        } catch (Exception e) {
            log.error("删除角色资源关联失败", e);
            return false;
        }
    }

    @Override
    public boolean saveBatch(List<SysRoleResourceEntity> roleResourceList) {
        if (roleResourceList == null || roleResourceList.isEmpty()) {
            return false;
        }
        
        try {
            for (SysRoleResourceEntity roleResource : roleResourceList) {
                if (roleResource.getId() == null) {
                    roleResource.setId(IdGenerator.snowflakeId());
                }
                sysRoleResourceMapper.insert(roleResource);
            }
            return true;
        } catch (Exception e) {
            log.error("批量保存角色资源关联失败", e);
            return false;
        }
    }

    @Override
    public boolean save(SysRoleResourceEntity roleResource) {
        if (roleResource == null) {
            return false;
        }
        
        try {
            if (roleResource.getId() == null) {
                roleResource.setId(IdGenerator.snowflakeId());
            }
            int result = sysRoleResourceMapper.insert(roleResource);
            return result > 0;
        } catch (Exception e) {
            log.error("保存角色资源关联失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRoleResources(Long roleId, List<Long> resourceIds) {
        if (roleId == null || resourceIds == null) {
            return false;
        }
        
        try {
            // 先删除原有关联
            deleteByRoleId(roleId);
            
            // 如果资源ID列表为空，则只删除不新增
            if (resourceIds.isEmpty()) {
                return true;
            }
            
            // 批量新增关联
            List<SysRoleResourceEntity> roleResourceList = new ArrayList<>();
            for (Long resourceId : resourceIds) {
                SysRoleResourceEntity roleResource = new SysRoleResourceEntity();
                roleResource.setId(IdGenerator.snowflakeId());
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResourceList.add(roleResource);
            }
            
            return saveBatch(roleResourceList);
        } catch (Exception e) {
            log.error("保存角色资源关联失败", e);
            throw new RuntimeException("保存角色资源关联失败", e);
        }
    }
}
