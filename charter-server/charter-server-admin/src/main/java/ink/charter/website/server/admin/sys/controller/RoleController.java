package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysRoleEntity;
import ink.charter.website.common.core.utils.StringUtils;
import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.domain.admin.api.dto.role.CreateRoleDTO;
import ink.charter.website.domain.admin.api.dto.role.PageRoleDTO;
import ink.charter.website.domain.admin.api.dto.role.UpdateRoleDTO;
import ink.charter.website.domain.admin.api.vo.role.RoleVO;
import ink.charter.website.server.admin.sys.converter.RoleConverter;
import ink.charter.website.server.admin.sys.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

/**
 * 角色管理控制器
 *
 * @author charter
 * @create 2025/11/05
 */
@Slf4j
@RestController
@RequestMapping("/sysRole")
@RequiredArgsConstructor
@Validated
@Tag(name = "角色管理", description = "角色管理相关接口")
public class RoleController {

    private final RoleService roleService;
    private final RoleConverter roleConverter;

    /**
     * 分页查询角色列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询角色列表", description = "分页查询角色列表")
    @PreAuthorize("hasAuthority('sys:role:page')")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.SELECT,
        description = "分页查询角色列表",
        recordParams = false
    )
    public Result<PageResult<RoleVO>> pageRoles(@RequestBody PageRoleDTO pageRequest) {
        return Result.success("查询成功", roleService.pageRoles(pageRequest));
    }

    /**
     * 根据ID获取角色信息
     */
    @GetMapping("/get")
    @Operation(summary = "获取角色信息", description = "根据角色ID获取角色详细信息")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.SELECT,
        description = "查询角色信息",
        recordParams = false
    )
    public Result<RoleVO> getById(@Parameter(description = "角色ID", required = true) @RequestParam Long id) {
        SysRoleEntity role = roleService.getById(id);
        
        if (role != null) {
            RoleVO roleVO = roleConverter.toVO(role);
            return Result.success("获取角色信息成功", roleVO);
        } else {
            return Result.error("角色不存在");
        }
    }

    /**
     * 根据角色编码获取角色信息
     */
    @GetMapping("/getByCode")
    @Operation(summary = "根据角色编码获取角色信息", description = "根据角色编码获取角色详细信息")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.SELECT,
        description = "根据角色编码查询角色",
        recordParams = false
    )
    public Result<RoleVO> getByRoleCode(@Parameter(description = "角色编码", required = true) @RequestParam String roleCode) {
        SysRoleEntity role = roleService.getByRoleCode(roleCode);
        
        if (role != null) {
            RoleVO roleVO = roleConverter.toVO(role);
            return Result.success("获取角色信息成功", roleVO);
        } else {
            return Result.error("角色不存在");
        }
    }

    /**
     * 查询所有启用的角色
     */
    @GetMapping("/listEnabled")
    @Operation(summary = "查询所有启用的角色", description = "查询所有启用的角色列表")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.SELECT,
        description = "查询所有启用的角色",
        recordParams = false
    )
    public Result<List<RoleVO>> listAllEnabled() {
        List<SysRoleEntity> roles = roleService.listAllEnabled();
        return Result.success("查询成功", roleConverter.toVOList(roles));
    }

    /**
     * 根据用户ID查询角色列表
     */
    @GetMapping("/listByUserId")
    @Operation(summary = "根据用户ID查询角色列表", description = "根据用户ID查询角色列表")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.SELECT,
        description = "查询用户角色列表",
        recordParams = false
    )
    public Result<List<RoleVO>> listByUserId(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        List<SysRoleEntity> roles = roleService.listByUserId(userId);
        return Result.success("查询成功", roleConverter.toVOList(roles));
    }

    /**
     * 创建角色
     */
    @PostMapping("/create")
    @Operation(summary = "创建角色", description = "创建新的角色")
    @PreAuthorize("hasAuthority('sys:role:create')")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.INSERT,
        description = "创建新角色"
    )
    public Result<RoleVO> create(@RequestBody @Validated CreateRoleDTO createRoleDTO) {
        SysRoleEntity role = roleConverter.toEntity(createRoleDTO);
        boolean success = roleService.create(role);
        
        if (success) {
            RoleVO roleVO = roleConverter.toVO(role);
            return Result.success("角色创建成功", roleVO);
        } else {
            return Result.error("角色创建失败");
        }
    }

    /**
     * 更新角色信息
     */
    @PostMapping("/update")
    @Operation(summary = "更新角色信息", description = "更新指定角色的信息")
    @PreAuthorize("hasAuthority('sys:role:update')")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.UPDATE,
        description = "更新角色信息"
    )
    public Result<RoleVO> update(@RequestBody @Validated UpdateRoleDTO updateRoleDTO) {
        Long roleId = updateRoleDTO.getId();
        SysRoleEntity existingRole = roleService.getById(roleId);
        if (existingRole == null) {
            return Result.error("角色不存在");
        }
        
        SysRoleEntity role = roleConverter.toEntity(updateRoleDTO);
        boolean success = roleService.update(role);
        
        if (success) {
            SysRoleEntity updatedRole = roleService.getById(roleId);
            RoleVO roleVO = roleConverter.toVO(updatedRole);
            return Result.success("角色信息更新成功", roleVO);
        } else {
            return Result.error("角色信息更新失败");
        }
    }

    /**
     * 删除角色
     */
    @PostMapping("/delete")
    @Operation(summary = "删除角色", description = "删除指定角色")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.DELETE,
        description = "删除角色"
    )
    public Result<Void> deleteById(@Parameter(description = "角色ID", required = true) @RequestParam Long id) {
        SysRoleEntity existingRole = roleService.getById(id);
        if (existingRole == null) {
            return Result.error("角色不存在");
        }
        
        boolean success = roleService.deleteById(id);
        
        if (success) {
            return Result.success("角色删除成功");
        } else {
            return Result.error("角色删除失败");
        }
    }

    /**
     * 批量删除角色
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除角色", description = "批量删除指定角色")
    @PreAuthorize("hasAuthority('sys:role:batch-delete')")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.DELETE,
        description = "批量删除角色"
    )
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            SysRoleEntity existingRole = roleService.getById(id);
            if (existingRole == null) {
                return Result.error("角色不存在");
            }
        }
        
        boolean success = roleService.batchDelete(ids);
        
        if (success) {
            return Result.success("角色批量删除成功");
        } else {
            return Result.error("角色批量删除失败");
        }
    }

    /**
     * 更新角色状态
     */
    @PostMapping("/change-status")
    @Operation(summary = "修改角色状态", description = "启用或禁用指定角色")
    @PreAuthorize("hasAuthority('sys:role:update-status')")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.UPDATE,
        description = "修改角色状态"
    )
    public Result<Void> updateStatus(@Parameter(description = "角色ID", required = true) @RequestParam Long id,
                                      @Parameter(description = "状态：0-禁用，1-启用", required = true) @RequestParam Integer status) {
        SysRoleEntity role = roleService.getById(id);
        if (role == null) {
            return Result.error("角色不存在");
        }
        
        boolean success = roleService.updateStatus(id, status);
        
        if (success) {
            String message = status == 1 ? "角色启用成功" : "角色禁用成功";
            return Result.success(message);
        } else {
            return Result.error("角色状态修改失败");
        }
    }

    /**
     * 保存用户角色关联
     */
    @PostMapping("/user/saveRoles")
    @Operation(summary = "用户分配角色权限", description = "保存用户角色关联关系")
    @PreAuthorize("hasAuthority('sys:role:user:save-roles')")
    @OperationLog(
        module = LogConstant.OptModule.ROLE,
        type = LogConstant.OptType.UPDATE,
        description = "保存用户角色关联"
    )
    public Result<Void> saveUserRoles(@Parameter(description = "用户ID", required = true) @RequestParam Long userId,
                                      @Parameter(description = "角色ID列表（用逗号分隔）") @RequestParam String roleIds) {
        List<Long> roleIdList = null;
        if (StringUtils.isNotBlank(roleIds)) {
            roleIdList = Stream.of(roleIds.split(","))
                    .map(Long::parseLong)
                    .toList();
        }
        
        roleService.saveUserRoles(userId, roleIdList);
        return Result.success("保存用户角色关联成功");
    }
}
