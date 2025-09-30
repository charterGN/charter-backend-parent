package ink.charter.website.common.auth.config;

import ink.charter.website.common.auth.interceptor.ResourcePermissionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 资源权限配置类
 * 配置资源权限拦截器和相关组件
 *
 * @author charter
 * @create 2025/09/30
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "charter.security.resource-permission.enabled", havingValue = "true", matchIfMissing = true)
public class ResourcePermissionConfig implements WebMvcConfigurer {

    private final ResourcePermissionInterceptor resourcePermissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(resourcePermissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 静态资源
                        "/static/**",
                        "/public/**",
                        "/assets/**",
                        "/favicon.ico",
                        
                        // API文档
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/doc.html",
                        
                        // 健康检查
                        "/actuator/**",
                        
                        // 认证相关接口
                        "/auth/login",
                        "/auth/logout",
                        "/auth/register",
                        "/auth/refresh",
                        
                        // 错误页面
                        "/error"
                )
                .order(100); // 设置拦截器顺序，确保在其他拦截器之后执行
    }
}