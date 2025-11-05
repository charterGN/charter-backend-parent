package ink.charter.website.common.auth.filter;

import ink.charter.website.common.auth.model.LoginUser;
import ink.charter.website.common.auth.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 获取请求中的token
        String token = getTokenFromRequest(request);
        
        if (StringUtils.hasText(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // 验证token有效性
                if (tokenService.verifyToken(token)) {
                    // 获取用户信息
                    LoginUser loginUser = tokenService.getLoginUserFromToken(token);
                    
                    if (loginUser != null) {
                        // 检查token是否在黑名单中
                        if (!tokenService.isTokenBlacklisted(token)) {
                            // 创建认证对象
                            UsernamePasswordAuthenticationToken authentication = 
                                    new UsernamePasswordAuthenticationToken(
                                            loginUser, 
                                            null, 
                                            loginUser.getAuthorities()
                                    );
                            
                            // 设置认证详情
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            
                            // 设置到安全上下文
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            
                            log.debug("用户 {} 认证成功", loginUser.getUsername());
                            
                            // 检查token是否即将过期，如果是则延长有效期
                            if (tokenService.isTokenExpiringSoon(token, 30)) {
                                tokenService.extendTokenExpire(token, 3600L); // 延长1小时
                                log.debug("延长用户 {} 的token有效期", loginUser.getUsername());
                            }
                        } else {
                            log.warn("Token已被加入黑名单: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Token验证失败: {}", e.getMessage());
                // 清除可能存在的认证信息
                SecurityContextHolder.clearContext();
            }
        }
        
        // 继续过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中获取token
     *
     * @param request HTTP请求
     * @return token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // 1. 从Header中获取
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        
        // 2. 从请求参数中获取（用于某些特殊场景，如WebSocket连接）
        String paramToken = request.getParameter("token");
        if (StringUtils.hasText(paramToken)) {
            return paramToken;
        }
        
        return null;
    }

    /**
     * 判断是否需要过滤
     * 对于某些不需要认证的路径，可以跳过过滤
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // 跳过登录、注册等公开接口
        return path.startsWith("/api-admin/auth/login") ||
               path.startsWith("/api-admin/auth/register") ||
               path.startsWith("/api-admin/auth/captcha") ||
               path.startsWith("/api-admin/public/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-ui.html") ||
               path.startsWith("/favicon.ico") ||
               path.startsWith("/error");
    }
}