package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysResourceEntity;
import ink.charter.website.common.core.utils.StringUtils;
import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.domain.admin.api.dto.resource.CreateResourceDTO;
import ink.charter.website.domain.admin.api.dto.resource.PageResourceDTO;
import ink.charter.website.domain.admin.api.dto.resource.UpdateResourceDTO;
import ink.charter.website.domain.admin.api.vo.resource.ResourceVO;
import ink.charter.website.server.admin.sys.converter.ResourceConverter;
import ink.charter.website.server.admin.sys.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

/**
 * 资源管理控制器
 *
 * @author charter
 * @create 2025/11/05
 */
@Slf4j
@RestController
@RequestMapping("/sysResource")
@RequiredArgsConstructor
@Validated
@Tag(name = "资源管理", description = "资源管理相关接口")
public class ResourceController {

    private final ResourceService resourceService;
    private final ResourceConverter resourceConverter;

    /**
     * 分页查询资源列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询资源列表", description = "分页查询资源列表")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.SELECT,
        description = "分页查询资源列表",
        recordParams = false
    )
    public Result<PageResult<ResourceVO>> pageResources(@RequestBody PageResourceDTO pageRequest) {
        return Result.success("查询成功", resourceService.pageResources(pageRequest));
    }

    /**
     * 根据ID获取资源信息
     */
    @GetMapping("/get")
    @Operation(summary = "获取资源信息", description = "根据资源ID获取资源详细信息")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.SELECT,
        description = "查询资源信息",
        recordParams = false
    )
    public Result<ResourceVO> getById(@Parameter(description = "资源ID", required = true) @RequestParam Long id) {
        SysResourceEntity resource = resourceService.getById(id);
        
        if (resource != null) {
            ResourceVO resourceVO = resourceConverter.toVO(resource);
            return Result.success("获取资源信息成功", resourceVO);
        } else {
            return Result.error("资源不存在");
        }
    }

    /**
     * 根据资源编码获取资源信息
     */
    @GetMapping("/getByCode")
    @Operation(summary = "根据资源编码获取资源信息", description = "根据资源编码获取资源详细信息")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.SELECT,
        description = "根据资源编码查询资源",
        recordParams = false
    )
    public Result<ResourceVO> getByResourceCode(@Parameter(description = "资源编码", required = true) @RequestParam String resourceCode) {
        SysResourceEntity resource = resourceService.getByResourceCode(resourceCode);
        
        if (resource != null) {
            ResourceVO resourceVO = resourceConverter.toVO(resource);
            return Result.success("获取资源信息成功", resourceVO);
        } else {
            return Result.error("资源不存在");
        }
    }

    /**
     * 根据资源类型查询资源列表
     */
    @GetMapping("/listByType")
    @Operation(summary = "根据资源类型查询资源列表", description = "根据资源类型查询资源列表")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.SELECT,
        description = "根据资源类型查询资源列表",
        recordParams = false
    )
    public Result<List<ResourceVO>> listByResourceType(@Parameter(description = "资源类型", required = true) @RequestParam Integer resourceType) {
        List<SysResourceEntity> resources = resourceService.listByResourceType(resourceType);
        return Result.success("查询成功", resourceConverter.toVOList(resources));
    }

    /**
     * 查询所有启用的资源
     */
    @GetMapping("/listEnabled")
    @Operation(summary = "查询所有启用的资源", description = "查询所有启用的资源列表")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.SELECT,
        description = "查询所有启用的资源",
        recordParams = false
    )
    public Result<List<ResourceVO>> listAllEnabled() {
        List<SysResourceEntity> resources = resourceService.listAllEnabled();
        return Result.success("查询成功", resourceConverter.toVOList(resources));
    }

    /**
     * 根据角色ID获取资源ID列表
     */
    @GetMapping("/role/resources")
    @Operation(summary = "根据角色ID获取资源ID列表", description = "根据角色ID获取资源ID列表")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.SELECT,
        description = "查询角色资源列表",
        recordParams = false
    )
    public Result<List<Long>> getResourceIdsByRoleId(@Parameter(description = "角色ID", required = true) @RequestParam Long roleId) {
        List<Long> resourceIds = resourceService.getResourceIdsByRoleId(roleId);
        return Result.success("查询成功", resourceIds);
    }

    /**
     * 创建资源
     */
    @PostMapping("/create")
    @Operation(summary = "创建资源", description = "创建新的资源")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.INSERT,
        description = "创建新资源"
    )
    public Result<ResourceVO> create(@RequestBody @Validated CreateResourceDTO createResourceDTO) {
        SysResourceEntity resource = resourceConverter.toEntity(createResourceDTO);
        boolean success = resourceService.create(resource);
        
        if (success) {
            ResourceVO resourceVO = resourceConverter.toVO(resource);
            return Result.success("资源创建成功", resourceVO);
        } else {
            return Result.error("资源创建失败");
        }
    }

    /**
     * 更新资源信息
     */
    @PostMapping("/update")
    @Operation(summary = "更新资源信息", description = "更新指定资源的信息")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.UPDATE,
        description = "更新资源信息"
    )
    public Result<ResourceVO> update(@RequestBody @Validated UpdateResourceDTO updateResourceDTO) {
        Long resourceId = updateResourceDTO.getId();
        SysResourceEntity existingResource = resourceService.getById(resourceId);
        if (existingResource == null) {
            return Result.error("资源不存在");
        }
        
        SysResourceEntity resource = resourceConverter.toEntity(updateResourceDTO);
        boolean success = resourceService.update(resource);
        
        if (success) {
            SysResourceEntity updatedResource = resourceService.getById(resourceId);
            ResourceVO resourceVO = resourceConverter.toVO(updatedResource);
            return Result.success("资源信息更新成功", resourceVO);
        } else {
            return Result.error("资源信息更新失败");
        }
    }

    /**
     * 删除资源
     */
    @PostMapping("/delete")
    @Operation(summary = "删除资源", description = "删除指定资源")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.DELETE,
        description = "删除资源"
    )
    public Result<Void> deleteById(@Parameter(description = "资源ID", required = true) @RequestParam Long id) {
        SysResourceEntity existingResource = resourceService.getById(id);
        if (existingResource == null) {
            return Result.error("资源不存在");
        }
        
        boolean success = resourceService.deleteById(id);
        
        if (success) {
            return Result.success("资源删除成功");
        } else {
            return Result.error("资源删除失败");
        }
    }

    /**
     * 批量删除资源
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除资源", description = "批量删除指定资源")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.DELETE,
        description = "批量删除资源"
    )
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            SysResourceEntity existingResource = resourceService.getById(id);
            if (existingResource == null) {
                return Result.error("资源不存在");
            }
        }
        
        boolean success = resourceService.batchDelete(ids);
        
        if (success) {
            return Result.success("资源批量删除成功");
        } else {
            return Result.error("资源批量删除失败");
        }
    }

    /**
     * 更新资源状态
     */
    @PostMapping("/change-status")
    @Operation(summary = "修改资源状态", description = "启用或禁用指定资源")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.UPDATE,
        description = "修改资源状态"
    )
    public Result<Void> updateStatus(@Parameter(description = "资源ID", required = true) @RequestParam Long id,
                                      @Parameter(description = "状态：0-禁用，1-启用", required = true) @RequestParam Integer status) {
        SysResourceEntity resource = resourceService.getById(id);
        if (resource == null) {
            return Result.error("资源不存在");
        }
        
        boolean success = resourceService.updateStatus(id, status);
        
        if (success) {
            String message = status == 1 ? "资源启用成功" : "资源禁用成功";
            return Result.success(message);
        } else {
            return Result.error("资源状态修改失败");
        }
    }

    /**
     * 保存角色资源关联
     */
    @PostMapping("/role/saveResources")
    @Operation(summary = "保存角色资源关联", description = "保存角色资源关联关系")
    @OperationLog(
        module = LogConstant.OptModule.RESOURCE,
        type = LogConstant.OptType.UPDATE,
        description = "保存角色资源关联"
    )
    public Result<Void> saveRoleResources(@Parameter(description = "角色ID", required = true) @RequestParam Long roleId,
                                          @Parameter(description = "资源ID列表（用逗号分隔）") @RequestParam String resourceIds) {
        List<Long> resourceIdList = null;
        if (StringUtils.isNotBlank(resourceIds)) {
            resourceIdList = Stream.of(resourceIds.split(","))
                    .map(Long::parseLong)
                    .toList();
        }

        resourceService.saveRoleResources(roleId, resourceIdList);
        return Result.success("保存角色资源关联成功");
    }
}
