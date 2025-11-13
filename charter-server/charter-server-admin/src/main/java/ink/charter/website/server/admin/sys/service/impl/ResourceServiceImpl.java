package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysResourceEntity;
import ink.charter.website.common.core.exception.BusinessException;
import ink.charter.website.common.core.utils.IdGenerator;
import ink.charter.website.domain.admin.api.dto.resource.PageResourceDTO;
import ink.charter.website.domain.admin.api.repository.SysResourceRepository;
import ink.charter.website.domain.admin.api.repository.SysRoleResourceRepository;
import ink.charter.website.domain.admin.api.vo.resource.ResourceVO;
import ink.charter.website.server.admin.sys.converter.ResourceConverter;
import ink.charter.website.server.admin.sys.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资源服务实现
 *
 * @author charter
 * @create 2025/11/05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final SysResourceRepository sysResourceRepository;
    private final SysRoleResourceRepository sysRoleResourceRepository;
    private final ResourceConverter resourceConverter;

    @Override
    public List<ResourceVO> pageResources(PageResourceDTO pageRequest) {
        List<SysResourceEntity> pageResult = sysResourceRepository.pageResources(pageRequest);
        return resourceConverter.toVOList(pageResult);
    }

    @Override
    public SysResourceEntity getById(Long id) {
        if (id == null) {
            throw BusinessException.paramInvalid("资源ID");
        }
        
        SysResourceEntity resource = sysResourceRepository.getById(id);
        if (resource == null) {
            throw BusinessException.dataNotFound("资源");
        }
        
        return resource;
    }

    @Override
    public SysResourceEntity getByResourceCode(String resourceCode) {
        return sysResourceRepository.getByResourceCode(resourceCode);
    }

    @Override
    public List<SysResourceEntity> listByModule(String module) {
        return sysResourceRepository.listByModule(module);
    }

    @Override
    public List<String> listAllModules() {
        return sysResourceRepository.listAllModules();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateResourceInfo(Long id, String resourceName, String description) {
        if (id == null) {
            throw BusinessException.paramInvalid("资源ID");
        }
        
        // 检查资源是否存在
        SysResourceEntity existResource = sysResourceRepository.getById(id);
        if (existResource == null) {
            throw BusinessException.dataNotFound("资源");
        }
        
        // 只更新名称和描述
        existResource.setResourceName(resourceName);
        existResource.setDescription(description);
        
        boolean result = sysResourceRepository.update(existResource);
        if (!result) {
            throw BusinessException.of("更新资源信息失败");
        }
        
        return true;
    }

    @Override
    public List<SysResourceEntity> listAllEnabled() {
        return sysResourceRepository.listAllEnabled();
    }

    @Override
    public List<Long> getResourceIdsByRoleId(Long roleId) {
        if (roleId == null) {
            throw BusinessException.paramInvalid("角色ID");
        }
        return sysRoleResourceRepository.getResourceIdsByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(SysResourceEntity resource) {
        if (resource == null) {
            throw BusinessException.paramInvalid("资源信息");
        }
        
        // 检查资源编码是否已存在
        if (sysResourceRepository.existsByResourceCode(resource.getResourceCode(), null)) {
            throw BusinessException.dataAlreadyExists("资源编码");
        }
        
        // 生成ID
        resource.setId(IdGenerator.snowflakeId());
        
        boolean result = sysResourceRepository.create(resource);
        if (!result) {
            throw BusinessException.of("创建资源失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SysResourceEntity resource) {
        if (resource == null || resource.getId() == null) {
            throw BusinessException.paramInvalid("资源信息");
        }
        
        // 检查资源是否存在
        SysResourceEntity existResource = sysResourceRepository.getById(resource.getId());
        if (existResource == null) {
            throw BusinessException.dataNotFound("资源");
        }
        
        // 检查资源编码是否已被其他资源使用
        if (sysResourceRepository.existsByResourceCode(resource.getResourceCode(), resource.getId())) {
            throw BusinessException.dataAlreadyExists("资源编码");
        }
        
        boolean result = sysResourceRepository.update(resource);
        if (!result) {
            throw BusinessException.of("更新资源失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        if (id == null) {
            throw BusinessException.paramInvalid("资源ID");
        }
        
        // 检查资源是否存在
        SysResourceEntity resource = sysResourceRepository.getById(id);
        if (resource == null) {
            throw BusinessException.dataNotFound("资源");
        }
        
        boolean result = sysResourceRepository.deleteById(id);
        if (!result) {
            throw BusinessException.of("删除资源失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw BusinessException.paramInvalid("资源ID列表");
        }
        
        boolean result = sysResourceRepository.batchDelete(ids);
        if (!result) {
            throw BusinessException.of("批量删除资源失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Integer status) {
        if (id == null) {
            throw BusinessException.paramInvalid("资源ID");
        }
        if (status == null) {
            throw BusinessException.paramInvalid("状态");
        }
        
        // 检查资源是否存在
        SysResourceEntity resource = sysResourceRepository.getById(id);
        if (resource == null) {
            throw BusinessException.dataNotFound("资源");
        }
        
        boolean result = sysResourceRepository.updateStatus(id, status);
        if (!result) {
            throw BusinessException.of("更新资源状态失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleResources(Long roleId, List<Long> resourceIds) {
        if (roleId == null) {
            throw BusinessException.paramInvalid("角色ID");
        }
        
        boolean result = sysRoleResourceRepository.saveRoleResources(roleId, resourceIds);
        if (!result) {
            throw BusinessException.of("保存角色资源关联失败");
        }
    }
}
