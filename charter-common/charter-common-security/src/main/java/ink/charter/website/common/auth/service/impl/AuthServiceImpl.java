package ink.charter.website.common.auth.service.impl;

import ink.charter.website.common.auth.config.AuthProperties;
import ink.charter.website.common.auth.model.LoginResponse;
import ink.charter.website.common.auth.model.LoginUser;
import ink.charter.website.common.auth.service.AuthService;
import ink.charter.website.common.auth.service.TokenService;
import ink.charter.website.common.auth.utils.SecurityUtils;
import ink.charter.website.common.core.utils.IpUtils;
import ink.charter.website.common.redis.service.RedisService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysUserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TokenService tokenService;
    private final AuthProperties authProperties;
    private final RedisService redisService;
    private final ApplicationContext applicationContext;
    
    // 懒加载服务，避免循环依赖
    private Object userService;
    private Object userSessionService;
    
    private static final String LOGIN_FAIL_KEY = "login:fail:";
    private static final String LOGIN_LOCK_KEY = "login:lock:";

    @Override
    public Result<LoginResponse> login(String usernameOrEmail, String password, String ip, String userAgent) {
        try {
            // 1. 参数验证
            if (!StringUtils.hasText(usernameOrEmail) || !StringUtils.hasText(password)) {
                return Result.error("用户名和密码不能为空");
            }
            
            // 2. 检查登录限制
            if (isLoginRestricted(usernameOrEmail)) {
                return Result.error("登录失败次数过多，账户已被锁定");
            }
            
            // 3. 获取用户服务
            Object userService = getUserService();
            if (userService == null) {
                return Result.error("用户服务不可用");
            }
            
            // 4. 查询用户信息
            SysUserEntity user = null;
            try {
                user = (SysUserEntity) userService.getClass().getMethod("getUserByUsernameOrEmail", String.class).invoke(userService, usernameOrEmail);
            } catch (Exception e) {
                log.error("查询用户信息失败: {}", e.getMessage(), e);
                return Result.error("查询用户信息失败");
            }
            
            // 5. 验证用户存在性
            if (user == null) {
                recordLoginFailure(usernameOrEmail);
                return Result.error("用户名或密码错误");
            }
            
            // 6. 验证密码
            boolean passwordValid = false;
            try {
                passwordValid = (Boolean) userService.getClass().getMethod("validatePassword", SysUserEntity.class, String.class).invoke(userService, user, password);
            } catch (Exception e) {
                log.error("验证密码失败: {}", e.getMessage(), e);
                return Result.error("密码验证失败");
            }
            
            if (!passwordValid) {
                recordLoginFailure(usernameOrEmail);
                return Result.error("用户名或密码错误");
            }
            
            // 7. 检查用户状态
            boolean statusNormal = false;
            try {
                statusNormal = (Boolean) userService.getClass().getMethod("isUserStatusNormal", SysUserEntity.class).invoke(userService, user);
            } catch (Exception e) {
                log.error("检查用户状态失败: {}", e.getMessage(), e);
                return Result.error("用户状态检查失败");
            }
            
            if (!statusNormal) {
                return Result.error("账户已被禁用");
            }
            
            // 8. 处理单点登录
            if (authProperties.getSession().getSingleLogin()) {
                Object userSessionService = getUserSessionService();
                if (userSessionService != null) {
                    try {
                        userSessionService.getClass().getMethod("invalidateUserSessions", Long.class).invoke(userSessionService, user.getId());
                    } catch (Exception e) {
                        log.warn("清理用户会话失败: {}", e.getMessage());
                    }
                }
            }
            
            // 9. 构建登录用户信息
            LoginUser loginUser = buildLoginUser(user);
            
            // 10. 生成Token
            String accessToken = tokenService.createToken(loginUser);
            String refreshToken = tokenService.createRefreshToken(loginUser);
            
            if (!StringUtils.hasText(accessToken)) {
                return Result.error("生成访问Token失败");
            }
            
            // 11. 创建会话记录
            Object userSessionService = getUserSessionService();
            if (userSessionService != null) {
                try {
                    LocalDateTime expireTime = LocalDateTime.now().plusSeconds(authProperties.getJwt().getAccessTokenExpire());
                    userSessionService.getClass().getMethod("createSession", Long.class, String.class, String.class, String.class, String.class, LocalDateTime.class)
                        .invoke(userSessionService, user.getId(), accessToken, refreshToken, ip, userAgent, expireTime);
                } catch (Exception e) {
                    log.warn("创建用户会话失败: {}", e.getMessage());
                }
            }
            
            // 12. 更新用户登录信息
            try {
                userService.getClass().getMethod("updateLoginInfo", Long.class, String.class).invoke(userService, user.getId(), ip);
            } catch (Exception e) {
                log.warn("更新用户登录信息失败: {}", e.getMessage());
            }
            
            // 13. 清除登录失败记录
            clearLoginRestriction(usernameOrEmail);
            
            // 14. 返回登录结果
            LoginResponse loginResponse = LoginResponse.of(accessToken, refreshToken, loginUser);
            return Result.success(loginResponse);
            
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage(), e);
            recordLoginFailure(usernameOrEmail);
            return Result.error("登录失败，请稍后重试");
        }
    }

    @Override
    public boolean logout(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return false;
            }
            
            // 1. 验证Token
            if (!tokenService.verifyToken(token)) {
                return false;
            }
            
            // 2. 删除Token
            tokenService.deleteToken(token);
            
            // 3. 使会话失效
            Object userSessionService = getUserSessionService();
            if (userSessionService != null) {
                try {
                    userSessionService.getClass().getMethod("invalidateSession", String.class).invoke(userSessionService, token);
                } catch (Exception e) {
                    log.warn("使会话失效失败: {}", e.getMessage());
                }
            }
            
            // 4. 清除Security上下文
            SecurityUtils.clearAuthentication();
            
            return true;
        } catch (Exception e) {
            log.error("登出失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Result<LoginResponse> refreshToken(String refreshToken) {
        try {
            if (!StringUtils.hasText(refreshToken)) {
                return Result.error("刷新Token不能为空");
            }
            
            // 1. 验证刷新Token
            if (!tokenService.verifyToken(refreshToken)) {
                return Result.error("刷新Token无效");
            }
            
            // 2. 获取用户信息
            Long userId = tokenService.getUserIdFromToken(refreshToken);
            String username = tokenService.getUsernameFromToken(refreshToken);
            
            if (userId == null || !StringUtils.hasText(username)) {
                return Result.error("Token信息无效");
            }
            
            // 3. 生成新的Token
            String newAccessToken = tokenService.refreshAccessToken(refreshToken);
            if (!StringUtils.hasText(newAccessToken)) {
                return Result.error("Token刷新失败");
            }
            
            // 4. 返回新Token
            LoginResponse loginResponse = LoginResponse.of(newAccessToken, refreshToken, null);
            return Result.success(loginResponse);
            
        } catch (Exception e) {
            log.error("刷新Token失败: {}", e.getMessage(), e);
            return Result.error("Token刷新失败");
        }
    }

    @Override
    public boolean validateToken(String token) {
        return tokenService.verifyToken(token);
    }

    @Override
    public boolean kickOutUser(Long userId) {
        try {
            if (userId == null) {
                return false;
            }
            
            // 1. 删除用户所有Token
            tokenService.deleteUserTokens(userId);
            
            // 2. 使用户所有会话失效
            Object userSessionService = getUserSessionService();
            if (userSessionService != null) {
                try {
                    userSessionService.getClass().getMethod("invalidateUserSessions", Long.class).invoke(userSessionService, userId);
                } catch (Exception e) {
                    log.warn("使用户会话失效失败: {}", e.getMessage());
                }
            }
            
            return true;
        } catch (Exception e) {
            log.error("踢出用户失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isLoginRestricted(String usernameOrEmail) {
        if (!StringUtils.hasText(usernameOrEmail)) {
            return false;
        }
        
        String lockKey = LOGIN_LOCK_KEY + usernameOrEmail;
        return Boolean.TRUE.equals(redisService.hasKey(lockKey));
    }

    @Override
    public void recordLoginFailure(String usernameOrEmail) {
        if (!StringUtils.hasText(usernameOrEmail)) {
            return;
        }
        
        try {
            String failKey = LOGIN_FAIL_KEY + usernameOrEmail;
            String lockKey = LOGIN_LOCK_KEY + usernameOrEmail;
            
            // 获取失败次数
            Integer failCount = (Integer) redisService.get(failKey);
            failCount = failCount == null ? 0 : failCount;
            failCount++;
            
            // 更新失败次数
            redisService.set(failKey, failCount, 1, TimeUnit.HOURS);
            
            // 检查是否需要锁定
            if (failCount >= authProperties.getLogin().getMaxAttempts()) {
                redisService.set(lockKey, true, authProperties.getLogin().getLockTime(), TimeUnit.SECONDS);
                log.warn("用户 {} 登录失败次数过多，已被锁定", usernameOrEmail);
            }
        } catch (Exception e) {
            log.error("记录登录失败失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public void clearLoginRestriction(String usernameOrEmail) {
        if (!StringUtils.hasText(usernameOrEmail)) {
            return;
        }
        
        try {
            String failKey = LOGIN_FAIL_KEY + usernameOrEmail;
            String lockKey = LOGIN_LOCK_KEY + usernameOrEmail;
            
            redisService.delete(failKey);
            redisService.delete(lockKey);
        } catch (Exception e) {
            log.error("清除登录限制失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 获取用户服务
     */
    private Object getUserService() {
        if (userService == null) {
            try {
                userService = applicationContext.getBean("userServiceImpl");
            } catch (Exception e) {
                log.warn("获取用户服务失败: {}", e.getMessage());
            }
        }
        return userService;
    }
    
    /**
     * 获取用户会话服务
     */
    private Object getUserSessionService() {
        if (userSessionService == null) {
            try {
                userSessionService = applicationContext.getBean("userSessionServiceImpl");
            } catch (Exception e) {
                log.warn("获取用户会话服务失败: {}", e.getMessage());
            }
        }
        return userSessionService;
    }
    
    /**
     * 构建登录用户信息
     */
    private LoginUser buildLoginUser(SysUserEntity user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setPassword(user.getPassword());
        loginUser.setStatus(user.getStatus());
        loginUser.setLoginTime(LocalDateTime.now());
        // 获取当前请求信息
        HttpServletRequest request = getCurrentRequest();
        loginUser.setLoginIp(request != null ? IpUtils.getClientIp(request) : "unknown");
        loginUser.setUserAgent(request != null ? request.getHeader("User-Agent") : "unknown");
        
        // 设置权限和角色
        Object userService = getUserService();
        if (userService != null) {
            try {
                @SuppressWarnings("unchecked")
                Set<String> permissions = (Set<String>) userService.getClass().getMethod("getUserPermissions", Long.class).invoke(userService, user.getId());
                @SuppressWarnings("unchecked")
                Set<String> roles = (Set<String>) userService.getClass().getMethod("getUserRoles", Long.class).invoke(userService, user.getId());
                loginUser.setPermissions(permissions);
                loginUser.setRoles(roles);
            } catch (Exception e) {
                log.warn("获取用户权限和角色失败: {}", e.getMessage());
                loginUser.setPermissions(Set.of());
                loginUser.setRoles(Set.of());
            }
        }
        
        return loginUser;
    }
    
    /**
     * 获取当前HTTP请求
     */
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            log.warn("获取HttpServletRequest失败: {}", e.getMessage());
            return null;
        }
    }
}