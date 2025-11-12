package ink.charter.website.common.auth.scanner;

import ink.charter.website.common.core.entity.sys.SysResourceEntity;
import ink.charter.website.common.core.utils.IdGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 资源扫描器
 * 项目启动时自动扫描所有Controller的@PreAuthorize注解，生成资源记录
 *
 * @author charter
 * @create 2025/11/12
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "charter.security.resource", name = "scan-enabled", havingValue = "true", matchIfMissing = true)
public class ResourceScanner implements ApplicationRunner {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final ResourceScannerCallback callback;

    /**
     * 权限码提取正则表达式
     * 支持两种格式：
     * 1. hasAuthority('xxx')
     * 2. 'xxx' (直接写权限码)
     */
    private static final Pattern AUTHORITY_PATTERN = Pattern.compile("hasAuthority\\(['\"]([^'\"]+)['\"]\\)");
    private static final Pattern DIRECT_PATTERN = Pattern.compile("^['\"]([^'\"]+)['\"]$");

    public ResourceScanner(RequestMappingHandlerMapping requestMappingHandlerMapping,
                          ResourceScannerCallback callback) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.callback = callback;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("========== 开始扫描资源权限 ==========");
        
        try {
            // 获取所有请求映射
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
            
            // 存储扫描到的资源
            List<SysResourceEntity> scannedResources = new ArrayList<>();
            
            // 遍历所有映射
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                RequestMappingInfo mappingInfo = entry.getKey();
                HandlerMethod handlerMethod = entry.getValue();
                
                // 解析资源信息
                SysResourceEntity resource = parseResource(mappingInfo, handlerMethod);
                if (resource != null) {
                    scannedResources.add(resource);
                }
            }
            
            // 通过回调接口同步到数据库
            callback.syncResources(scannedResources);
            
            log.info("========== 资源权限扫描完成，共扫描到 {} 个资源 ==========", scannedResources.size());
        } catch (Exception e) {
            log.error("资源权限扫描失败", e);
        }
    }

    /**
     * 解析资源信息
     */
    private SysResourceEntity parseResource(RequestMappingInfo mappingInfo, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        
        // 检查是否有@PreAuthorize注解
        PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(method, PreAuthorize.class);
        if (preAuthorize == null) {
            return null;
        }
        
        // 提取权限码
        String authorityExpression = preAuthorize.value();
        String resourceCode = extractAuthority(authorityExpression);
        if (!StringUtils.hasText(resourceCode)) {
            log.warn("无法从表达式中提取权限码: {}", authorityExpression);
            return null;
        }
        
        // 获取模块名称（从类的@Tag注解）
        Class<?> controllerClass = handlerMethod.getBeanType();
        Tag tag = AnnotationUtils.findAnnotation(controllerClass, Tag.class);
        String module = tag != null ? tag.name() : "未分类";
        
        // 获取资源名称和描述（从方法的@Operation注解）
        Operation operation = AnnotationUtils.findAnnotation(method, Operation.class);
        String resourceName = operation != null ? operation.summary() : method.getName();
        String description = operation != null ? operation.description() : "";
        
        // 获取URL和HTTP方法
        Set<String> patterns = mappingInfo.getPatternValues();
        String url = patterns.isEmpty() ? "" : patterns.iterator().next();
        
        Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
        String httpMethod = methods.isEmpty() ? "GET" : methods.iterator().next().name();
        
        // 构建资源实体
        SysResourceEntity resource = new SysResourceEntity();
        resource.setResourceCode(resourceCode);
        resource.setResourceName(resourceName);
        resource.setModule(module);
        resource.setUrl(url);
        resource.setMethod(httpMethod);
        resource.setDescription(description);
        resource.setStatus(1); // 默认启用
        
        return resource;
    }

    /**
     * 从权限表达式中提取权限码
     * 支持两种格式：
     * 1. hasAuthority('user:create')
     * 2. 'user:create' (直接写权限码)
     */
    private String extractAuthority(String expression) {
        if (!StringUtils.hasText(expression)) {
            return null;
        }
        
        // 1. 尝试匹配 hasAuthority('xxx')
        Matcher authorityMatcher = AUTHORITY_PATTERN.matcher(expression);
        if (authorityMatcher.find()) {
            return authorityMatcher.group(1);
        }
        
        // 2. 尝试匹配直接写的权限码 'xxx'
        Matcher directMatcher = DIRECT_PATTERN.matcher(expression.trim());
        if (directMatcher.find()) {
            return directMatcher.group(1);
        }
        
        return null;
    }
}
