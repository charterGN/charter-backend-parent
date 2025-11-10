package ink.charter.website.domain.admin.core.repository.impl;

import ink.charter.website.common.core.entity.sys.SysUserRoleEntity;
import ink.charter.website.common.core.utils.IdGenerator;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.admin.api.repository.SysUserRoleRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysUserRoleMapper;
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
 * 用户角色关联领域仓库实现
 *
 * @author charter
 * @create 2025/11/05
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysUserRoleRepositoryImpl implements SysUserRoleRepository {

    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        
        List<SysUserRoleEntity> userRoleList = sysUserRoleMapper.selectByUserId(userId);
        return userRoleList.stream()
                .map(SysUserRoleEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        if (userId == null) {
            return false;
        }
        
        try {
            sysUserRoleMapper.deleteByUserId(userId);
            return true;
        } catch (Exception e) {
            log.error("删除用户角色关联失败", e);
            return false;
        }
    }

    @Override
    public boolean saveBatch(List<SysUserRoleEntity> userRoleList) {
        if (userRoleList == null || userRoleList.isEmpty()) {
            return false;
        }
        
        try {
            for (SysUserRoleEntity userRole : userRoleList) {
                if (userRole.getId() == null) {
                    userRole.setId(IdGenerator.snowflakeId());
                }
                sysUserRoleMapper.insert(userRole);
            }
            return true;
        } catch (Exception e) {
            log.error("批量保存用户角色关联失败", e);
            return false;
        }
    }

    @Override
    public boolean save(SysUserRoleEntity userRole) {
        if (userRole == null) {
            return false;
        }
        
        try {
            if (userRole.getId() == null) {
                userRole.setId(IdGenerator.snowflakeId());
            }
            int result = sysUserRoleMapper.insert(userRole);
            return result > 0;
        } catch (Exception e) {
            log.error("保存用户角色关联失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserRoles(Long userId, List<Long> roleIds) {
        if (userId == null) {
            return false;
        }
        
        try {
            // 查询数据库中已有的角色ID
            List<Long> existingRoleIds = getRoleIdsByUserId(userId);
            Set<Long> existingSet = new HashSet<>(existingRoleIds);
            Set<Long> newSet = roleIds != null ? new HashSet<>(roleIds) : new HashSet<>();
            
            // 计算需要新增的角色ID（新分配的 - 已有的）
            Set<Long> toAdd = new HashSet<>(newSet);
            toAdd.removeAll(existingSet);
            
            // 计算需要删除的角色ID（已有的 - 新分配的）
            Set<Long> toDelete = new HashSet<>(existingSet);
            toDelete.removeAll(newSet);
            
            // 删除不再需要的关联
            if (!toDelete.isEmpty()) {
                sysUserRoleMapper.delete(QueryWrappers.<SysUserRoleEntity>lambdaQuery()
                        .eq(SysUserRoleEntity::getUserId, userId)
                        .in(SysUserRoleEntity::getRoleId, toDelete));
            }
            
            // 新增需要的关联
            if (!toAdd.isEmpty()) {
                List<SysUserRoleEntity> userRoleList = new ArrayList<>();
                for (Long roleId : toAdd) {
                    SysUserRoleEntity userRole = new SysUserRoleEntity();
                    userRole.setId(IdGenerator.snowflakeId());
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    userRoleList.add(userRole);
                }
                saveBatch(userRoleList);
            }
            
            return true;
        } catch (Exception e) {
            log.error("保存用户角色关联失败", e);
            throw new RuntimeException("保存用户角色关联失败", e);
        }
    }
}
