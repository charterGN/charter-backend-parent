package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.domain.admin.api.dto.user.*;
import ink.charter.website.server.admin.sys.service.UserService;
import ink.charter.website.server.admin.sys.converter.UserConverter;
import ink.charter.website.domain.admin.api.vo.user.UserPermissionsVO;
import ink.charter.website.domain.admin.api.vo.user.UserVO;
import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysUserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 用户管理控制器
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    /**
     * 分页查询用户列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表")
    public Result<PageResult<UserVO>> pageUsers(@RequestBody PageUserDTO pageRequest) {
        return Result.success("查询成功", userService.pageUsers(pageRequest));
    }

    /**
     * 创建用户
     */
    @PostMapping("/create")
    @Operation(summary = "创建用户", description = "创建新用户")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.INSERT,
        description = "创建新用户"
    )
    public Result<UserVO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        // 用户名、邮箱唯一性校验
        if (userService.getUserByUsername(createUserDTO.getUsername()) != null) {
            return Result.error("该用户名已存在");
        }
        if (userService.getUserByEmail(createUserDTO.getEmail()) != null) {
            return Result.error("该邮箱已存在");
        }

        SysUserEntity user = userConverter.toEntity(createUserDTO);
        boolean success = userService.createUser(user);

        if (success) {
            UserVO userVO = userConverter.toVO(user);
            return Result.success("用户创建成功", userVO);
        } else {
            return Result.error("用户创建失败");
        }
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    @Operation(summary = "更新用户信息", description = "更新指定用户的信息")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.UPDATE,
        description = "更新用户信息"
    )
    public Result<UserVO> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        Long userId = updateUserDTO.getUserId();
        SysUserEntity existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }

        // 用户名、邮箱唯一性校验
        if (userService.getUserByUsername(updateUserDTO.getUsername()) != null) {
            return Result.error("该用户名已存在");
        }
        if (userService.getUserByEmail(updateUserDTO.getEmail()) != null) {
            return Result.error("该邮箱已存在");
        }

        SysUserEntity user = userConverter.toEntity(updateUserDTO);
        boolean success = userService.updateUser(user);

        if (success) {
            SysUserEntity updatedUser = userService.getUserById(userId);
            UserVO userVO = userConverter.toVO(updatedUser);
            return Result.success("用户信息更新成功", userVO);
        } else {
            return Result.error("用户信息更新失败");
        }
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @Operation(summary = "删除用户", description = "删除指定用户")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.DELETE,
        description = "删除用户"
    )
    public Result<Void> deleteUser(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        SysUserEntity existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }

        boolean success = userService.deleteUser(userId);

        if (success) {
            return Result.success("用户删除成功");
        } else {
            return Result.error("用户删除失败");
        }
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除用户", description = "批量删除指定用户")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.DELETE,
        description = "批量删除用户"
    )
    public Result<Void> batchDeleteUsers(@RequestBody List<Long> userIds) {
        for (Long userId : userIds) {
            SysUserEntity existingUser = userService.getUserById(userId);
            if (existingUser == null) {
                return Result.error("用户不存在");
            }
        }

        boolean success = userService.batchDeleteUsers(userIds);

        if (success) {
            return Result.success("用户批量删除成功");
        } else {
            return Result.error("用户批量删除失败");
        }
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/get")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.SELECT,
        description = "查询用户信息",
        recordParams = false
    )
    public Result<UserVO> getUserById(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        SysUserEntity user = userService.getUserById(userId);

        if (user != null) {
            UserVO userVO = userConverter.toVO(user);
            return Result.success("获取用户信息成功", userVO);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/getByUsername")
    @Operation(summary = "根据用户名获取用户信息", description = "根据用户名获取用户详细信息")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.SELECT,
        description = "根据用户名查询用户",
        recordParams = false
    )
    public Result<UserVO> getUserByUsername(@Parameter(description = "用户名", required = true) @RequestParam String username) {
        SysUserEntity user = userService.getUserByUsername(username);

        if (user != null) {
            UserVO userVO = userConverter.toVO(user);
            return Result.success("获取用户信息成功", userVO);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 获取用户权限列表
     */
    @GetMapping("/permissions")
    @Operation(summary = "获取用户权限", description = "获取指定用户的权限列表")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.SELECT,
        description = "查询用户权限",
        recordParams = false
    )
    public Result<UserPermissionsVO> getUserPermissions(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        SysUserEntity user = userService.getUserById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        Set<String> roles = userService.getUserRoles(userId);
        Set<String> permissions = userService.getUserPermissions(userId);
        UserPermissionsVO permissionsVO = userConverter.toPermissionsVO(user, roles, permissions);
        return Result.success("获取用户权限成功", permissionsVO);
    }

    /**
     * 修改用户密码
     */
    @PostMapping("/change-password")
    @Operation(summary = "修改用户密码", description = "修改指定用户的密码")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.UPDATE,
        description = "修改用户密码"
    )
    public Result<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        SysUserEntity user = userService.getUserById(changePasswordDTO.getUserId());
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 验证旧密码
        if (!userService.validatePassword(user, changePasswordDTO.getOldPassword())) {
            return Result.error("原密码错误");
        }

        // 更新密码
        user.setPassword(changePasswordDTO.getNewPassword());
        boolean success = userService.updateUser(user);

        if (success) {
            return Result.success("密码修改成功");
        } else {
            return Result.error("密码修改失败");
        }
    }

    /**
     * 启用/禁用用户
     */
    @PostMapping("/change-status")
    @Operation(summary = "修改用户状态", description = "启用或禁用指定用户")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.UPDATE,
        description = "修改用户状态"
    )
    public Result<Void> changeUserStatus(@RequestBody ChangeUserStatusDTO changeUserStatusDTO) {
        SysUserEntity user = userService.getUserById(changeUserStatusDTO.getUserId());
        if (user == null) {
            return Result.error("用户不存在");
        }

        user.setStatus(changeUserStatusDTO.getStatus());
        boolean success = userService.updateUser(user);

        if (success) {
            String message = changeUserStatusDTO.getStatus() == 1 ? "用户启用成功" : "用户禁用成功";
            return Result.success(message);
        } else {
            return Result.error("用户状态修改失败");
        }
    }
}