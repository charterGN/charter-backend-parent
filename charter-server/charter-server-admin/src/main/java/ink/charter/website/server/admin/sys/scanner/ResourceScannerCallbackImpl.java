package ink.charter.website.server.admin.sys.scanner;

import ink.charter.website.common.auth.scanner.ResourceScannerCallback;
import ink.charter.website.common.core.entity.sys.SysResourceEntity;
import ink.charter.website.common.core.utils.IdGenerator;
import ink.charter.website.domain.admin.api.repository.SysResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源扫描器回调实现
 * 负责将扫描到的资源同步到数据库
 *
 * @author charter
 * @create 2025/11/12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceScannerCallbackImpl implements ResourceScannerCallback {

    private final SysResourceRepository sysResourceRepository;

    @Override
    public void syncResources(List<SysResourceEntity> scannedResources) {
        int insertCount = 0;
        int updateCount = 0;
        int deleteCount = 0;
        
        // 收集扫描到的所有权限码
        Set<String> scannedResourceCodes = scannedResources.stream()
                .map(SysResourceEntity::getResourceCode)
                .collect(Collectors.toSet());
        
        // 1. 新增或更新资源
        for (SysResourceEntity resource : scannedResources) {
            try {
                // 检查资源是否已存在
                SysResourceEntity existingResource = sysResourceRepository.getByResourceCode(resource.getResourceCode());
                
                if (existingResource == null) {
                    // 新增资源
                    resource.setId(IdGenerator.snowflakeId());
                    sysResourceRepository.create(resource);
                    insertCount++;
                    log.debug("新增资源: {} - {}", resource.getResourceCode(), resource.getResourceName());
                } else {
                    // 更新资源信息（保持原有状态和用户自定义的名称、描述）
                    existingResource.setModule(resource.getModule());
                    existingResource.setUrl(resource.getUrl());
                    existingResource.setMethod(resource.getMethod());
                    sysResourceRepository.update(existingResource);
                    updateCount++;
                    log.debug("更新资源: {} - {}", resource.getResourceCode(), resource.getResourceName());
                }
            } catch (Exception e) {
                log.error("同步资源失败: {}", resource.getResourceCode(), e);
            }
        }
        
        // 2. 删除孤儿资源（数据库中存在但未被扫描到的资源）
        try {
            List<SysResourceEntity> allResources = sysResourceRepository.listAllEnabled();
            for (SysResourceEntity existingResource : allResources) {
                if (!scannedResourceCodes.contains(existingResource.getResourceCode())) {
                    // 该资源在数据库中存在，但未被扫描到，说明对应的接口已被删除
                    sysResourceRepository.deleteById(existingResource.getId());
                    deleteCount++;
                    log.debug("删除孤儿资源: {} - {}", existingResource.getResourceCode(), existingResource.getResourceName());
                }
            }
        } catch (Exception e) {
            log.error("删除孤儿资源失败", e);
        }
        
        log.info("资源同步完成 - 新增: {}, 更新: {}, 删除: {}", insertCount, updateCount, deleteCount);
    }
}
