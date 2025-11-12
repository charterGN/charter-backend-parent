package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.common.core.utils.StringUtils;
import ink.charter.website.domain.admin.api.dto.menu.CreateMenuDTO;
import ink.charter.website.domain.admin.api.dto.menu.PageMenuDTO;
import ink.charter.website.domain.admin.api.dto.menu.UpdateMenuDTO;
import ink.charter.website.server.admin.sys.converter.MenuConverter;
import ink.charter.website.server.admin.sys.service.MenuService;
import ink.charter.website.domain.admin.api.vo.menu.DynamicMenuVO;
import ink.charter.website.domain.admin.api.vo.menu.MenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final MenuConverter menuConverter;

    /**
     * 分页查询菜单
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询菜单", description = "分页查询菜单列表")
    @PreAuthorize("hasAuthority('sys:menu:page')")
    public Result<PageResult<MenuVO>> pageMenus(@RequestBody PageMenuDTO pageRequest) {
        PageResult<MenuVO> result = menuService.pageMenus(pageRequest);
        return Result.success(result);
    }

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
    public Result<List<MenuVO>> listMenuNormal() {
        List<MenuVO> menuList = menuService.listMenuNormal();
        return Result.success(menuList);
    }

    /**
     * 查询所有菜单列表
     */
    @GetMapping("/listAll")
    @Operation(summary = "查询所有菜单列表", description = "查询所有菜单列表")
    public Result<List<MenuVO>> listAll() {
        List<MenuVO> menuList = menuService.listAll();
        return Result.success(menuList);
    }

    /**
     * 根据ID查询菜单
     */
    @GetMapping("/getById")
    @Operation(summary = "根据ID查询菜单", description = "根据菜单ID查询菜单详情")
    public Result<MenuVO> getById(@Parameter(description = "菜单ID", required = true) @RequestParam Long id) {
        MenuVO menu = menuService.getById(id);
        return Result.success(menu);
    }

    /**
     * 根据父菜单ID查询子菜单列表
     */
    @GetMapping("/listByParentId")
    @Operation(summary = "根据父菜单ID查询子菜单列表", description = "根据父菜单ID查询子菜单列表")
    public Result<List<MenuVO>> listByParentId(
            @Parameter(description = "父菜单ID", required = true) @RequestParam Long parentId) {
        List<MenuVO> menuList = menuService.listByParentId(parentId);
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
     * 创建菜单
     */
    @PostMapping("/create")
    @Operation(summary = "创建菜单", description = "创建新菜单")
    @PreAuthorize("hasAuthority('sys:menu:create')")
    public Result<MenuVO> create(@RequestBody @Validated CreateMenuDTO createMenuDTO) {
        SysMenuEntity menu = menuConverter.toEntity(createMenuDTO);
        menuService.create(menu);
        MenuVO menuVO = menuConverter.convertToMenuVO(menu);
        return Result.success(menuVO);
    }

    /**
     * 更新菜单
     */
    @PostMapping("/update")
    @Operation(summary = "更新菜单", description = "更新菜单信息")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result<MenuVO> update(@RequestBody @Validated UpdateMenuDTO updateMenuDTO) {
        SysMenuEntity menu = menuConverter.toEntity(updateMenuDTO);
        menuService.update(menu);
        MenuVO menuVO = menuService.getById(menu.getId());
        return Result.success(menuVO);
    }

    /**
     * 删除菜单
     */
    @PostMapping("/delete")
    @Operation(summary = "删除菜单", description = "根据菜单ID删除菜单")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result<Void> delete(@Parameter(description = "菜单ID", required = true) @RequestParam Long id) {
        menuService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除菜单
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除菜单", description = "批量删除菜单")
    @PreAuthorize("hasAuthority('sys:menu:batch-delete')")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        menuService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 更新菜单状态
     */
    @PostMapping("/updateStatus/{id}/{status}")
    @Operation(summary = "更新菜单状态", description = "更新菜单状态")
    @PreAuthorize("hasAuthority('sys:menu:update-status')")
    public Result<Void> updateStatus(
            @Parameter(description = "菜单ID", required = true) @PathVariable Long id,
            @Parameter(description = "状态：0-禁用，1-启用", required = true) @PathVariable Integer status) {
        menuService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 保存角色菜单关联
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表（用逗号分隔）
     * @return 操作结果
     */
    @PostMapping("/saveRoleMenu/{roleId}/{menuIds}")
    @Operation(summary = "角色分配菜单权限", description = "保存角色与菜单的关联关系")
    @PreAuthorize("hasAuthority('sys:menu:save-role-menu')")
    public Result<Void> saveRoleMenu(
            @Parameter(description = "角色ID", required = true)
            @PathVariable Long roleId,
            @Parameter(description = "菜单ID列表（用逗号分隔）")
            @PathVariable String menuIds) {
        // 处理菜单ID列表
        List<Long> menuIdList = null;
        if (StringUtils.isNotBlank(menuIds)) {
            menuIdList = java.util.Arrays.stream(menuIds.split(","))
                    .filter(id -> !id.trim().isEmpty() && !id.equals("-1"))
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());
        }

        menuService.saveRoleMenu(roleId, menuIdList);
        return Result.success();
    }
}
