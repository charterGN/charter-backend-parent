package ink.charter.website.common.auth.handler;

import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.common.enums.ResCodeEnum;
import ink.charter.website.common.core.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 认证入口点实现
 * 处理未认证的请求
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response, 
                        AuthenticationException authException) throws IOException {
        
        log.warn("认证失败，请求路径: {}, 错误信息: {}", request.getRequestURI(), authException.getMessage());
        
        // 设置响应状态码和内容类型
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        // 构建错误响应
        Result<Void> result = Result.error(ResCodeEnum.LOGIN_REQUIRED);
        
        // 写入响应
        response.getWriter().write(JsonUtils.toJsonString(result));
    }
}