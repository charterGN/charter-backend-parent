package ink.charter.website.domain.admin.core.repository.impl;

import ink.charter.website.common.core.entity.sys.SysRoleResourceEntity;
import ink.charter.website.common.core.utils.IdGenerator;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.admin.api.repository.SysRoleResourceRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysRoleResourceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        if (roleId == null) {
            return false;
        }
        
        try {
            // 查询数据库中已有的资源ID
            List<Long> existingResourceIds = getResourceIdsByRoleId(roleId);
            Set<Long> existingSet = new HashSet<>(existingResourceIds);
            Set<Long> newSet = resourceIds != null ? new HashSet<>(resourceIds) : new HashSet<>();
            
            // 计算需要新增的资源ID（新分配的 - 已有的）
            Set<Long> toAdd = new HashSet<>(newSet);
            toAdd.removeAll(existingSet);
            
            // 计算需要删除的资源ID（已有的 - 新分配的）
            Set<Long> toDelete = new HashSet<>(existingSet);
            toDelete.removeAll(newSet);
            
            // 删除不再需要的关联
            if (!toDelete.isEmpty()) {
                sysRoleResourceMapper.delete(QueryWrappers.<SysRoleResourceEntity>lambdaQuery()
                        .eq(SysRoleResourceEntity::getRoleId, roleId)
                        .in(SysRoleResourceEntity::getResourceId, toDelete));
            }
            
            // 新增需要的关联
            if (!toAdd.isEmpty()) {
                List<SysRoleResourceEntity> roleResourceList = new ArrayList<>();
                for (Long resourceId : toAdd) {
                    SysRoleResourceEntity roleResource = new SysRoleResourceEntity();
                    roleResource.setId(IdGenerator.snowflakeId());
                    roleResource.setRoleId(roleId);
                    roleResource.setResourceId(resourceId);
                    roleResourceList.add(roleResource);
                }
                saveBatch(roleResourceList);
            }
            
            return true;
        } catch (Exception e) {
            log.error("保存角色资源关联失败", e);
            throw new RuntimeException("保存角色资源关联失败", e);
        }
    }
}
