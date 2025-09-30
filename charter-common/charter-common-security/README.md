# Charter Common Security

Charter项目的通用安全认证模块，提供完整的用户认证、授权和权限控制功能。

## 功能特性

### 1. 用户认证
- JWT令牌认证
- 用户登录/登出
- 密码加密存储
- 用户状态管理

### 2. JWT令牌管理
- 令牌生成与验证
- 令牌刷新机制
- 令牌黑名单管理
- 自动过期处理

### 3. 会话管理
- Redis会话存储
- 会话超时控制
- 多设备登录管理
- 会话状态同步

### 4. 权限控制
- **RBAC权限模型**：基于角色的访问控制
- **方法级注解**：支持`@PreAuthorize`、`@PostAuthorize`等Spring Security注解
- **资源权限管理**：细粒度的资源访问控制
- **动态权限验证**：支持运行时权限检查

### 5. 资源权限控制组件（新增）
- **注解式权限控制**：
  - `@RequireResource`：资源权限注解
  - `@RequirePermission`：通用权限注解
- **权限服务**：
  - `ResourcePermissionService`：资源权限验证服务
  - 支持单个、任意、全部资源权限检查
  - 基于URL和HTTP方法的权限验证
  - Redis缓存优化
- **权限拦截器**：
  - `ResourcePermissionInterceptor`：自动权限验证拦截器
  - 支持方法级、类级、URL级权限控制
- **工具类**：
  - `ResourcePermissionUtils`：便捷的权限检查工具
  - 支持编程式权限验证

## 核心组件

### 认证相关
- `LoginUser`：登录用户信息模型
- `AuthService`：认证服务接口
- `TokenService`：令牌服务接口
- `UserDetailsServiceImpl`：Spring Security用户详情服务

### 安全配置
- `SecurityConfig`：Spring Security核心配置
- `SecurityAutoConfiguration`：自动配置类
- `ResourcePermissionConfig`：资源权限配置

### 过滤器和拦截器
- `JwtAuthenticationFilter`：JWT认证过滤器
- `ResourcePermissionInterceptor`：资源权限拦截器

### 异常处理
- `AuthenticationEntryPointImpl`：认证入口点
- `AccessDeniedHandlerImpl`：访问拒绝处理器
- `ResourcePermissionException`：资源权限异常

### 工具类
- `SecurityUtils`：安全工具类
- `JwtUtils`：JWT工具类
- `ResourcePermissionUtils`：资源权限工具类

## 使用方式

### 1. 基本配置

在`application.yml`中配置：

```yaml
charter:
  auth:
    enabled: true
    jwt:
      secret: your-jwt-secret
      expiration: 86400000  # 24小时
    redis:
      enabled: true
  security:
    resource-permission:
      enabled: true  # 启用资源权限控制
```

### 2. 注解使用

#### 资源权限注解
```java
@RestController
@RequireResource("user:manage")  // 类级别权限控制
public class UserController {
    
    @GetMapping("/list")
    @RequireResource("user:list")  // 方法级别权限控制
    public Result<List<User>> list() {
        // ...
    }
    
    @PostMapping("/save")
    @RequireResource(value = {"user:add", "user:edit"}, requireAll = false)  // 任意一个权限
    public Result<Void> save(@RequestBody User user) {
        // ...
    }
    
    @DeleteMapping("/{id}")
    @RequireResource(value = {"user:delete", "admin:manage"}, requireAll = true)  // 需要所有权限
    public Result<Void> delete(@PathVariable Long id) {
        // ...
    }
}
```

#### 通用权限注解
```java
@RestController
public class AdminController {
    
    @GetMapping("/dashboard")
    @RequirePermission(
        roles = {"admin", "manager"}, 
        resources = {"dashboard:view"},
        requireBoth = false  // 满足角色或资源权限任意一种
    )
    public Result<DashboardData> dashboard() {
        // ...
    }
    
    @PostMapping("/system/config")
    @RequirePermission(
        roles = {"admin"}, 
        resources = {"system:config"},
        requireBoth = true  // 同时需要角色和资源权限
    )
    public Result<Void> updateConfig(@RequestBody SystemConfig config) {
        // ...
    }
}
```

### 3. 编程式权限检查

```java
@Service
public class UserService {
    
    public void deleteUser(Long userId) {
        // 检查权限
        ResourcePermissionUtils.requireResourcePermission("user:delete");
        
        // 或者检查多个权限
        ResourcePermissionUtils.requireAnyResourcePermission("user:delete", "admin:manage");
        
        // 业务逻辑
        // ...
    }
    
    public List<User> getUserList() {
        // 条件检查
        if (ResourcePermissionUtils.hasResourcePermission("user:list")) {
            return getAllUsers();
        } else {
            return getPublicUsers();
        }
    }
}
```

### 4. 权限服务使用

```java
@Service
public class BusinessService {
    
    @Autowired
    private ResourcePermissionService resourcePermissionService;
    
    public void processData() {
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        
        // 检查用户权限
        if (resourcePermissionService.hasResourcePermission(currentUser, "data:process")) {
            // 执行业务逻辑
        }
        
        // 获取用户所有资源权限
        Set<String> permissions = resourcePermissionService.getUserResourcePermissions(currentUser);
        
        // 验证请求权限
        boolean hasPermission = resourcePermissionService.validateRequestPermission(
            "/api/users", "GET", currentUser
        );
    }
}
```

## 数据库表结构

资源权限控制依赖以下数据库表：

- `sys_user`：用户表
- `sys_role`：角色表
- `sys_resource`：资源表
- `sys_user_role`：用户角色关联表
- `sys_role_resource`：角色资源关联表

## 缓存策略

- 用户权限信息缓存：`user:permissions:{userId}`
- 资源代码缓存：`resource:codes:{url}:{method}`
- 缓存过期时间：30分钟（可配置）

## 注意事项

1. 确保数据库表结构正确创建
2. Redis服务正常运行
3. 正确配置JWT密钥
4. 权限注解优先级：方法级 > 类级 > URL级
5. 资源权限控制可通过配置禁用，不影响现有功能

## 扩展说明

本模块采用模块化设计，各组件职责清晰，便于扩展和维护：

- 支持自定义权限验证逻辑
- 支持多种权限存储方式
- 支持权限缓存策略自定义
- 支持权限异常处理自定义