package ink.charter.website.common.auth.config;

import ink.charter.website.common.auth.filter.JwtAuthenticationFilter;
import ink.charter.website.common.auth.handler.AccessDeniedHandlerImpl;
import ink.charter.website.common.auth.handler.AuthenticationEntryPointImpl;
import ink.charter.website.common.auth.interceptor.ResourcePermissionInterceptor;
import ink.charter.website.common.auth.service.AuthService;
import ink.charter.website.common.auth.service.ResourcePermissionService;
import ink.charter.website.common.auth.service.TokenService;
import ink.charter.website.common.auth.service.impl.AuthServiceImpl;
import ink.charter.website.common.auth.service.impl.ResourcePermissionServiceImpl;
import ink.charter.website.common.auth.service.impl.TokenServiceImpl;
import ink.charter.website.common.auth.service.impl.UserDetailsServiceImpl;
import ink.charter.website.common.auth.utils.JwtUtils;
import ink.charter.website.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全认证自动配置类
 * 自动装配安全认证相关组件
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@ConditionalOnClass(name = "org.springframework.security.config.annotation.web.configuration.EnableWebSecurity")
@Import({SecurityConfig.class, ResourcePermissionConfig.class})
public class SecurityAutoConfiguration {

    /**
     * 注册认证配置属性
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthProperties authProperties() {
        log.info("初始化认证配置属性");
        return new AuthProperties();
    }

    /**
     * 注册密码编码器
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        log.info("初始化密码编码器");
        return new BCryptPasswordEncoder();
    }

    /**
     * 注册JWT工具类
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtUtils jwtUtils(AuthProperties authProperties) {
        log.info("初始化JWT工具类");
        return new JwtUtils(authProperties);
    }

    /**
     * 注册令牌服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
    public TokenService tokenService(JwtUtils jwtUtils, AuthProperties authProperties, RedisService redisService) {
        log.info("初始化令牌服务");
        return new TokenServiceImpl(jwtUtils, authProperties, redisService);
    }

    /**
     * 注册认证服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
    public AuthService authService(TokenService tokenService, AuthProperties authProperties, RedisService redisService, ApplicationContext applicationContext) {
        log.info("初始化认证服务");
        return new AuthServiceImpl(tokenService, authProperties, redisService, applicationContext);
    }

    /**
     * 注册用户详情服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
    public UserDetailsService userDetailsService(ApplicationContext applicationContext) {
        log.info("初始化用户详情服务");
        return new UserDetailsServiceImpl(applicationContext);
    }

    /**
     * 注册认证入口点处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPointImpl authenticationEntryPoint() {
        log.info("初始化认证入口点处理器");
        return new AuthenticationEntryPointImpl();
    }

    /**
     * 注册访问拒绝处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public AccessDeniedHandlerImpl accessDeniedHandler() {
        log.info("初始化访问拒绝处理器");
        return new AccessDeniedHandlerImpl();
    }

    /**
     * 注册JWT认证过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenService tokenService) {
        log.info("初始化JWT认证过滤器");
        return new JwtAuthenticationFilter(tokenService);
    }

    /**
     * 注册资源权限服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.auth", name = "resource-permission-enabled", havingValue = "true", matchIfMissing = true)
    public ResourcePermissionService resourcePermissionService() {
        log.info("初始化资源权限服务");
        return new ResourcePermissionServiceImpl();
    }

    /**
     * 注册资源权限拦截器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.auth", name = "resource-permission-enabled", havingValue = "true", matchIfMissing = true)
    public ResourcePermissionInterceptor resourcePermissionInterceptor(ResourcePermissionService resourcePermissionService) {
        log.info("初始化资源权限拦截器");
        return new ResourcePermissionInterceptor(resourcePermissionService);
    }
}