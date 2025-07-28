package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.server.admin.sys.service.MenuService;
import ink.charter.website.server.admin.sys.vo.menu.DynamicMenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 * @author charter
 * @create 2025/07/28
 */
@Slf4j
@RestController
@RequestMapping("/sysMenu")
@RequiredArgsConstructor
@Validated
@Tag(name = "菜单管理", description = "动态菜单、菜单管理等相关接口")
public class MenuController {

    private final MenuService menuService;

    /**
     * 获取用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @GetMapping("/listRouters/{userId}")
    @Operation(summary = "获取用户菜单列表", description = "根据用户ID获取该用户可访问的菜单列表")
    public Result<List<DynamicMenuVO>> listRouters(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId) {
        List<DynamicMenuVO> menuList = menuService.listRouters(userId);
        return Result.success(menuList);
    }

    /**
     * 获取所有正常菜单列表
     *
     * @return 菜单列表
     */
    @GetMapping("/listMenuNormal")
    @Operation(summary = "获取所有正常菜单列表", description = "获取所有状态正常的菜单列表")
    public Result<List<SysMenuEntity>> listMenuNormal() {
        List<SysMenuEntity> menuList = menuService.listMenuNormal();
        return Result.success(menuList);
    }

    /**
     * 根据角色ID获取菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    @GetMapping("/listMenuIdsByRoleId/{roleId}")
    @Operation(summary = "根据角色ID获取菜单ID列表", description = "根据角色ID获取该角色拥有的菜单ID列表")
    public Result<List<Long>> listMenuIdsByRoleId(
            @Parameter(description = "角色ID", required = true)
            @PathVariable Long roleId) {
        List<Long> menuIds = menuService.listMenuIdsByRoleId(roleId);
        return Result.success(menuIds);
    }

    /**
     * 保存角色菜单关联
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表（用逗号分隔）
     * @return 操作结果
     */
    @PostMapping("/saveRoleMenu/{roleId}/{menuIds}")
    @Operation(summary = "保存角色菜单关联", description = "保存角色与菜单的关联关系")
    public Result<Void> saveRoleMenu(
            @Parameter(description = "角色ID", required = true)
            @PathVariable Long roleId,
            @Parameter(description = "菜单ID列表（用逗号分隔）", required = true)
            @PathVariable String menuIds) {
        // 处理菜单ID列表
        List<Long> menuIdList = java.util.Arrays.stream(menuIds.split(","))
                .filter(id -> !id.trim().isEmpty() && !id.equals("-1"))
                .map(Long::valueOf)
                .collect(java.util.stream.Collectors.toList());
        
        menuService.saveRoleMenu(roleId, menuIdList);
        return Result.success();
    }
}
