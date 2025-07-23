package ink.charter.website.common.auth.service.impl;

import ink.charter.website.common.auth.model.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Spring Security用户详情服务实现
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ApplicationContext applicationContext;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            if (!StringUtils.hasText(username)) {
                throw new UsernameNotFoundException("用户名不能为空");
            }
            
            // 通过ApplicationContext获取UserService，避免循环依赖
            Object userService;
            try {
                userService = applicationContext.getBean("userServiceImpl");
            } catch (Exception e) {
                log.error("获取用户服务失败: {}", e.getMessage(), e);
                throw new UsernameNotFoundException("用户服务不可用: " + username);
            }
            
            // 查询用户信息
            Object user;
            try {
                user = userService.getClass().getMethod("getUserByUsernameOrEmail", String.class).invoke(userService, username);
            } catch (Exception e) {
                log.error("查询用户信息失败: {}", e.getMessage(), e);
                throw new UsernameNotFoundException("查询用户信息失败: " + username);
            }
            
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在: " + username);
            }
            
            // 检查用户状态
            boolean statusNormal;
            try {
                statusNormal = (Boolean) userService.getClass().getMethod("isUserStatusNormal", user.getClass()).invoke(userService, user);
            } catch (Exception e) {
                log.error("检查用户状态失败: {}", e.getMessage(), e);
                throw new UsernameNotFoundException("用户状态检查失败: " + username);
            }
            
            if (!statusNormal) {
                throw new UsernameNotFoundException("用户已被禁用: " + username);
            }
            
            // 查询用户权限和角色
            Set<String> permissions = Set.of();
            Set<String> roles = Set.of();
            try {
                Long userId = (Long) user.getClass().getMethod("getId").invoke(user);
                @SuppressWarnings("unchecked")
                Set<String> userPermissions = (Set<String>) userService.getClass().getMethod("getUserPermissions", Long.class).invoke(userService, userId);
                @SuppressWarnings("unchecked")
                Set<String> userRoles = (Set<String>) userService.getClass().getMethod("getUserRoles", Long.class).invoke(userService, userId);
                permissions = userPermissions != null ? userPermissions : Set.of();
                roles = userRoles != null ? userRoles : Set.of();
            } catch (Exception e) {
                log.warn("获取用户权限和角色失败: {}", e.getMessage());
            }
            
            // 构建LoginUser
            return buildLoginUser(user, permissions, roles);
            
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("加载用户信息失败: {}", e.getMessage(), e);
            throw new UsernameNotFoundException("加载用户信息失败: " + username, e);
        }
    }
    
    /**
     * 构建登录用户信息
     */
    private LoginUser buildLoginUser(Object user, Set<String> permissions, Set<String> roles) {
        LoginUser loginUser = new LoginUser();
        
        try {
            // 设置基本信息
            Long userId = (Long) user.getClass().getMethod("getId").invoke(user);
            String username = (String) user.getClass().getMethod("getUsername").invoke(user);
            String password = (String) user.getClass().getMethod("getPassword").invoke(user);
            Integer status = (Integer) user.getClass().getMethod("getStatus").invoke(user);
            
            loginUser.setUserId(userId);
            loginUser.setUsername(username);
            loginUser.setPassword(password);
            loginUser.setStatus(status);
            
        } catch (Exception e) {
            log.error("构建登录用户信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("构建登录用户信息失败", e);
        }
        
        // 设置权限和角色
        loginUser.setPermissions(permissions);
        loginUser.setRoles(roles);
        
        // 设置登录信息
        loginUser.setLoginTime(LocalDateTime.now());
        
        return loginUser;
    }
}