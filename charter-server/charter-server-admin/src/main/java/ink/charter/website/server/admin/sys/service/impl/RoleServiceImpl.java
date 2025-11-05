package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysRoleEntity;
import ink.charter.website.common.core.exception.BusinessException;
import ink.charter.website.common.core.utils.IdGenerator;
import ink.charter.website.domain.admin.api.dto.role.PageRoleDTO;
import ink.charter.website.domain.admin.api.repository.SysRoleRepository;
import ink.charter.website.domain.admin.api.repository.SysUserRoleRepository;
import ink.charter.website.domain.admin.api.vo.role.RoleVO;
import ink.charter.website.server.admin.sys.converter.RoleConverter;
import ink.charter.website.server.admin.sys.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现
 *
 * @author charter
 * @create 2025/11/05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final SysRoleRepository sysRoleRepository;
    private final SysUserRoleRepository sysUserRoleRepository;
    private final RoleConverter roleConverter;

    @Override
    public PageResult<RoleVO> pageRoles(PageRoleDTO pageRequest) {
        PageResult<SysRoleEntity> pageResult = sysRoleRepository.pageRoles(pageRequest);
        List<RoleVO> voList = roleConverter.toVOList(pageResult.getRecords());
        return PageResult.of(voList, pageResult.getTotal());
    }

    @Override
    public SysRoleEntity getById(Long id) {
        if (id == null) {
            throw BusinessException.paramInvalid("角色ID");
        }
        
        SysRoleEntity role = sysRoleRepository.getById(id);
        if (role == null) {
            throw BusinessException.dataNotFound("角色");
        }
        
        return role;
    }

    @Override
    public SysRoleEntity getByRoleCode(String roleCode) {
        return sysRoleRepository.getByRoleCode(roleCode);
    }

    @Override
    public List<SysRoleEntity> listAllEnabled() {
        return sysRoleRepository.listAllEnabled();
    }

    @Override
    public List<SysRoleEntity> listByUserId(Long userId) {
        if (userId == null) {
            throw BusinessException.paramInvalid("用户ID");
        }
        return sysRoleRepository.listByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(SysRoleEntity role) {
        if (role == null) {
            throw BusinessException.paramInvalid("角色信息");
        }
        
        // 检查角色编码是否已存在
        if (sysRoleRepository.existsByRoleCode(role.getRoleCode(), null)) {
            throw BusinessException.dataAlreadyExists("角色编码");
        }
        
        // 生成ID
        role.setId(IdGenerator.snowflakeId());
        
        boolean result = sysRoleRepository.create(role);
        if (!result) {
            throw BusinessException.of("创建角色失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SysRoleEntity role) {
        if (role == null || role.getId() == null) {
            throw BusinessException.paramInvalid("角色信息");
        }
        
        // 检查角色是否存在
        SysRoleEntity existRole = sysRoleRepository.getById(role.getId());
        if (existRole == null) {
            throw BusinessException.dataNotFound("角色");
        }
        
        // 检查角色编码是否已被其他角色使用
        if (sysRoleRepository.existsByRoleCode(role.getRoleCode(), role.getId())) {
            throw BusinessException.dataAlreadyExists("角色编码");
        }
        
        boolean result = sysRoleRepository.update(role);
        if (!result) {
            throw BusinessException.of("更新角色失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        if (id == null) {
            throw BusinessException.paramInvalid("角色ID");
        }
        
        // 检查角色是否存在
        SysRoleEntity role = sysRoleRepository.getById(id);
        if (role == null) {
            throw BusinessException.dataNotFound("角色");
        }
        
        boolean result = sysRoleRepository.deleteById(id);
        if (!result) {
            throw BusinessException.of("删除角色失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw BusinessException.paramInvalid("角色ID列表");
        }
        
        boolean result = sysRoleRepository.batchDelete(ids);
        if (!result) {
            throw BusinessException.of("批量删除角色失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Integer status) {
        if (id == null) {
            throw BusinessException.paramInvalid("角色ID");
        }
        if (status == null) {
            throw BusinessException.paramInvalid("状态");
        }
        
        // 检查角色是否存在
        SysRoleEntity role = sysRoleRepository.getById(id);
        if (role == null) {
            throw BusinessException.dataNotFound("角色");
        }
        
        boolean result = sysRoleRepository.updateStatus(id, status);
        if (!result) {
            throw BusinessException.of("更新角色状态失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoles(Long userId, List<Long> roleIds) {
        if (userId == null) {
            throw BusinessException.paramInvalid("用户ID");
        }
        
        boolean result = sysUserRoleRepository.saveUserRoles(userId, roleIds);
        if (!result) {
            throw BusinessException.of("保存用户角色关联失败");
        }
    }
}
