package ink.charter.website.common.auth.handler;

import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.common.enums.ResCodeEnum;
import ink.charter.website.common.core.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 访问拒绝处理器实现
 * 处理权限不足的请求
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, 
                      HttpServletResponse response, 
                      AccessDeniedException accessDeniedException) throws IOException {
        
        log.warn("访问被拒绝，请求路径: {}, 错误信息: {}", request.getRequestURI(), accessDeniedException.getMessage());
        
        // 设置响应状态码和内容类型
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        // 构建错误响应
        Result<Void> result = Result.error(ResCodeEnum.PERMISSION_DENIED);
        
        // 写入响应
        response.getWriter().write(JsonUtils.toJsonString(result));
    }
}