# Charter 认证模块 (common-security)

## 概述

本模块提供了完整的用户认证和授权功能，基于 Spring Security 和 JWT 实现，支持用户登录、登出、令牌刷新、会话管理等功能。

## 主要功能

### 1. 用户认证
- 支持用户名/邮箱 + 密码登录
- 密码加密存储（BCrypt + 盐值）
- 登录失败次数限制和账户锁定
- 单点登录控制

### 2. JWT 令牌管理
- 访问令牌（Access Token）和刷新令牌（Refresh Token）
- 令牌自动续期
- 令牌黑名单机制
- 令牌过期检测和清理

### 3. 会话管理
- 用户会话跟踪
- 多设备登录管理
- 会话失效和踢出功能
- 会话过期自动清理

### 4. 权限控制
- 基于角色的访问控制（RBAC）
- 方法级权限注解支持
- 资源权限管理
- 动态权限验证

## 核心组件

### 服务层
- `AuthService`: 认证服务接口
- `TokenService`: 令牌管理服务接口
- `AuthServiceImpl`: 认证服务实现
- `TokenServiceImpl`: 令牌管理服务实现
- `UserDetailsServiceImpl`: Spring Security 用户详情服务

### 配置类
- `SecurityConfig`: Spring Security 配置
- `AuthProperties`: 认证相关配置属性

### 过滤器
- `JwtAuthenticationFilter`: JWT 认证过滤器

### 异常处理
- `AuthenticationEntryPointImpl`: 认证失败处理器
- `AccessDeniedHandlerImpl`: 访问拒绝处理器

### 工具类
- `SecurityUtils`: 安全工具类
- `JwtUtils`: JWT 工具类

### 模型类
- `LoginUser`: 登录用户信息模型

## 配置说明

在 `application.yml` 中添加以下配置：

```yaml
charter:
  auth:
    jwt:
      # JWT 密钥
      secret: your-jwt-secret-key-here
      # 访问令牌过期时间（秒）
      access-token-expire: 7200
      # 刷新令牌过期时间（秒）
      refresh-token-expire: 604800
      # 令牌即将过期阈值（秒）
      expire-threshold: 1800
    login:
      # 是否启用单点登录
      single-login: false
      # 登录失败最大次数
      max-fail-count: 5
      # 账户锁定时间（秒）
      lock-time: 1800
    session:
      # 会话超时时间（秒）
      timeout: 7200
      # 最大并发会话数
      max-sessions: 10
```

## 使用示例

### 1. 用户登录

```java
@RestController
@RequestMapping("/api-admin/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ip = IpUtils.getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        Result<LoginResponse> result = authService.login(
            request.getUsername(), 
            request.getPassword(), 
            ip, 
            userAgent
        );
        
        return result;
    }
}
```

### 2. 获取当前用户信息

```java
@GetMapping("/current-user")
public Result<UserInfo> getCurrentUser() {
    Long userId = SecurityUtils.getCurrentUserId();
    String username = SecurityUtils.getCurrentUsername();
    
    UserInfo userInfo = new UserInfo(userId, username);
    return Result.success(userInfo);
}
```

### 3. 权限验证

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/users")
public Result<List<User>> getUsers() {
    // 只有管理员角色可以访问
    return Result.success(userService.getAllUsers());
}

@PreAuthorize("hasPermission('user:delete')")
@PostMapping("/users/{id}")
public Result<Void> deleteUser(@PathVariable Long id) {
    // 需要用户删除权限
    userService.deleteUser(id);
    return Result.success();
}
```

### 4. 令牌刷新

```java
@PostMapping("/refresh")
public Result<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
    Result<LoginResponse> result = authService.refreshToken(request.getRefreshToken());
    return result;
}
```

## 数据库表结构

### 用户表 (sys_user)
```sql
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `email` VARCHAR(100) COMMENT '邮箱',
  `phone` VARCHAR(20) COMMENT '手机号',
  `avatar` VARCHAR(500) COMMENT '头像URL',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
  `salt` VARCHAR(32) COMMENT '密码盐值',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `login_count` INT DEFAULT 0 COMMENT '登录次数',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
  -- 其他字段...
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
);
```

### 用户会话表 (sys_user_session)
```sql
CREATE TABLE `sys_user_session` (
  `id` BIGINT NOT NULL COMMENT '会话ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `session_id` VARCHAR(128) NOT NULL COMMENT '会话标识',
  `token` VARCHAR(500) COMMENT '访问令牌',
  `refresh_token` VARCHAR(500) COMMENT '刷新令牌',
  `login_ip` VARCHAR(50) COMMENT '登录IP',
  `login_address` VARCHAR(200) COMMENT '登录地址',
  `user_agent` VARCHAR(500) COMMENT '用户代理',
  `login_time` DATETIME NOT NULL COMMENT '登录时间',
  `expire_time` DATETIME NOT NULL COMMENT '过期时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0离线 1在线）',
  -- 其他字段...
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_session_id` (`session_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_token` (`token`(100))
);
```

## 安全注意事项

1. **JWT 密钥安全**: 确保 JWT 密钥足够复杂且定期更换
2. **密码存储**: 使用 BCrypt + 盐值双重加密
3. **令牌传输**: 建议使用 HTTPS 传输令牌
4. **会话管理**: 定期清理过期会话和令牌
5. **权限控制**: 遵循最小权限原则
6. **日志记录**: 记录所有认证和授权相关操作

## 扩展功能

- 支持第三方登录（OAuth2）
- 双因子认证（2FA）
- 验证码登录
- 记住我功能
- 设备指纹识别

## 依赖项

- Spring Security
- Spring Boot
- JWT (jsonwebtoken)
- Redis (用于缓存)
- MyBatis Plus (数据访问)
- BCrypt (密码加密)

## 版本历史

- v1.0.0: 初始版本，基础认证功能
- v1.1.0: 添加会话管理和令牌刷新
- v1.2.0: 完善权限控制和异常处理