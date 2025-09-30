package ink.charter.website.common.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录用户信息
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 密码（加密）
     */
    @JsonIgnore
    private String password;

    /**
     * 用户状态（0禁用 1启用）
     */
    private Integer status;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 角色列表
     */
    private Set<String> roles;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 会话ID
     */
    private String sessionId;

    public LoginUser(Long userId, String username, String password, Integer status, 
                     Set<String> permissions, Set<String> roles) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.permissions = permissions;
        this.roles = roles;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 合并权限和角色
        List<GrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        // 添加角色权限（角色需要以ROLE_开头）
        roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .forEach(authorities::add);
        
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return status != null && status == 1;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return status != null && status == 1;
    }

    /**
     * 检查用户是否有指定权限
     *
     * @param permission 权限
     * @return 是否有权限
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }

    /**
     * 检查用户是否有指定角色
     *
     * @param role 角色
     * @return 是否有角色
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    /**
     * 检查用户是否有任意一个指定权限
     *
     * @param permissions 权限列表
     * @return 是否有权限
     */
    public boolean hasAnyPermission(String... permissions) {
        if (this.permissions == null || permissions == null) {
            return false;
        }
        for (String permission : permissions) {
            if (this.permissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查用户是否有任意一个指定角色
     *
     * @param roles 角色列表
     * @return 是否有角色
     */
    public boolean hasAnyRole(String... roles) {
        if (this.roles == null || roles == null) {
            return false;
        }
        for (String role : roles) {
            if (this.roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查用户是否拥有所有指定角色
     *
     * @param roles 角色列表
     * @return 是否拥有所有角色
     */
    public boolean hasAllRoles(String... roles) {
        if (this.roles == null || roles == null) {
            return false;
        }
        for (String role : roles) {
            if (!this.roles.contains(role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查用户是否拥有所有指定权限
     *
     * @param permissions 权限列表
     * @return 是否拥有所有权限
     */
    public boolean hasAllPermissions(String... permissions) {
        if (this.permissions == null || permissions == null) {
            return false;
        }
        for (String permission : permissions) {
            if (!this.permissions.contains(permission)) {
                return false;
            }
        }
        return true;
    }
}