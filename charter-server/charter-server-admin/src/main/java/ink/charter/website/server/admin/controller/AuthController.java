package ink.charter.website.server.admin.controller;

import ink.charter.website.server.admin.converter.AuthConverter;
import ink.charter.website.server.admin.dto.auth.LoginRequestDTO;
import ink.charter.website.server.admin.dto.auth.RefreshTokenRequestDTO;
import ink.charter.website.server.admin.vo.auth.LoginResponseVO;
import ink.charter.website.server.admin.vo.auth.TokenValidationVO;
import ink.charter.website.server.admin.vo.auth.UserInfoVO;
import ink.charter.website.common.auth.model.LoginResponse;
import ink.charter.website.common.auth.model.LoginUser;
import ink.charter.website.common.auth.service.AuthService;
import ink.charter.website.common.auth.utils.SecurityUtils;
import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.common.log.utils.LogUtils;
import ink.charter.website.common.core.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



/**
 * 认证控制器
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@RestController
@RequestMapping("/api-admin/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "认证管理", description = "用户登录、登出、Token刷新等认证相关接口")
public class AuthController {

    private final AuthService authService;
    private final AuthConverter authConverter;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户通过用户名/邮箱和密码进行登录")
    @OperationLog(
        module = LogConstant.OptModule.AUTH,
        type = LogConstant.OptType.LOGIN,
        description = "用户登录系统",
        recordResponse = false
    )
    public Result<LoginResponseVO> login(@RequestBody LoginRequestDTO request, HttpServletRequest httpRequest) {
        try {
            // 获取客户端信息
            String clientIp = LogUtils.getClientIp(httpRequest);
            String userAgent = LogUtils.getUserAgent(httpRequest);
            
            // 执行登录
            Result<LoginResponse> loginResult = authService.login(
                request.getUsernameOrEmail(), 
                request.getPassword(), 
                clientIp, 
                userAgent
            );
            
            if (loginResult.isSuccess()) {
                LoginResponseVO responseVO = authConverter.toLoginResponseVO(loginResult.getData());
                return Result.success("登录成功", responseVO);
            } else {
                return Result.error(loginResult.getMessage());
            }
            
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage(), e);
            return Result.error("登录失败，请稍后重试");
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出，清除Token和会话信息")
    @OperationLog(
        module = LogConstant.OptModule.AUTH,
        type = LogConstant.OptType.LOGOUT,
        description = "用户登出系统",
        recordParams = false
    )
    public Result<Void> logout(HttpServletRequest request) {
        try {
            // 从请求头获取Token
            String token = getTokenFromRequest(request);
            
            if (StringUtils.hasText(token)) {
                boolean success = authService.logout(token);
                if (success) {
                    return Result.success("登出成功");
                } else {
                    return Result.error("登出失败");
                }
            } else {
                return Result.error("未找到有效的Token");
            }
            
        } catch (Exception e) {
            log.error("登出失败: {}", e.getMessage(), e);
            return Result.error("登出失败，请稍后重试");
        }
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "使用刷新Token获取新的访问Token")
    @OperationLog(
        module = LogConstant.OptModule.AUTH,
        type = LogConstant.OptType.OTHER,
        description = "刷新访问令牌",
        recordParams = false,
        recordResponse = false
    )
    public Result<LoginResponseVO> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        try {
            Result<LoginResponse> result = authService.refreshToken(request.getRefreshToken());
            
            if (result.isSuccess()) {
                LoginResponseVO responseVO = authConverter.toLoginResponseVO(result.getData());
                return Result.success("Token刷新成功", responseVO);
            } else {
                return Result.error(result.getMessage());
            }
            
        } catch (Exception e) {
            log.error("刷新Token失败: {}", e.getMessage(), e);
            return Result.error("Token刷新失败，请稍后重试");
        }
    }

    /**
     * 验证Token
     */
    @GetMapping("/validate")
    @Operation(summary = "验证Token", description = "验证当前Token是否有效")
    @OperationLog(
        module = LogConstant.OptModule.AUTH,
        type = LogConstant.OptType.OTHER,
        description = "验证访问令牌",
        recordParams = false,
        recordResponse = false
    )
    public Result<TokenValidationVO> validateToken(HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            
            if (StringUtils.hasText(token)) {
                boolean valid = authService.validateToken(token);
                if (valid) {
                    // 获取当前用户信息
                    Long userId = SecurityUtils.getCurrentUserId();
                    String username = SecurityUtils.getCurrentUsername();
                    TokenValidationVO validationVO = TokenValidationVO.valid(userId, username, null, false);
                    return Result.success("Token有效", validationVO);
                } else {
                    return Result.success("Token无效", TokenValidationVO.invalid());
                }
            } else {
                return Result.success("未找到Token", TokenValidationVO.invalid());
            }
            
        } catch (Exception e) {
            log.error("验证Token失败: {}", e.getMessage(), e);
            return Result.error("Token验证失败");
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current-user")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @OperationLog(
        module = LogConstant.OptModule.AUTH,
        type = LogConstant.OptType.SELECT,
        description = "获取当前用户信息",
        recordParams = false
    )
    public Result<UserInfoVO> getCurrentUser() {
        try {
            // 从SecurityContext获取当前用户信息
            LoginUser loginUser = SecurityUtils.getCurrentUser();
            
            if (loginUser != null) {
                UserInfoVO userInfoVO = authConverter.toUserInfoVO(loginUser);
                return Result.success("获取用户信息成功", userInfoVO);
            } else {
                return Result.error("用户未登录");
            }
            
        } catch (Exception e) {
            log.error("获取当前用户信息失败: {}", e.getMessage(), e);
            return Result.error("获取用户信息失败");
        }
    }

    /**
     * 踢出用户
     */
    @PostMapping("/kick-out-user")
    @Operation(summary = "踢出用户", description = "强制用户下线，清除其所有Token和会话")
    @OperationLog(
        module = LogConstant.OptModule.AUTH,
        type = LogConstant.OptType.OTHER,
        description = "踢出用户"
    )
    public Result<Void> kickOutUser(@Parameter(description = "用户ID") @RequestParam Long userId) {
        try {
            boolean success = authService.kickOutUser(userId);
            
            if (success) {
                return Result.success("用户已被踢出");
            } else {
                return Result.error("踢出用户失败");
            }
            
        } catch (Exception e) {
            log.error("踢出用户失败: {}", e.getMessage(), e);
            return Result.error("踢出用户失败，请稍后重试");
        }
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}