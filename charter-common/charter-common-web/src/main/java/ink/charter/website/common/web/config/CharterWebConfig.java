package ink.charter.website.common.web.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.Set;

/**
 * Web配置类
 * 用于配置Web相关的设置
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiPrefixProperties.class)
public class CharterWebConfig implements WebMvcConfigurer {

    private final ApiPrefixProperties apiPrefixProperties;

    /**
     * 配置路径匹配
     * 根据配置属性为不同模块的Controller添加相应的API前缀
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        if (!apiPrefixProperties.isEnabled()) {
            log.info("API前缀配置已禁用");
            return;
        }

        // 用于记录已应用前缀的包，避免重复日志
        Set<String> appliedPackages = new HashSet<>();

        // 为每个配置的模块添加前缀
        apiPrefixProperties.getModules().forEach((pattern, prefix) -> {
            configurer.addPathPrefix(prefix, c -> {
                String packageName = c.getPackageName();
                boolean matches = matchesPattern(packageName, pattern);
                
                // 只在第一次匹配时记录日志，避免重复输出
                if (matches && !appliedPackages.contains(packageName + ":" + prefix)) {
                    log.info("为包 {} 应用API前缀: {}", packageName, prefix);
                    appliedPackages.add(packageName + ":" + prefix);
                } else if (log.isDebugEnabled()) {
                    log.debug("检查Controller包名: {} 匹配模式: {} 结果: {}", packageName, pattern, matches);
                }
                
                return matches;
            });
        });
        
        log.info("API前缀配置已启用，配置的模块: {}", apiPrefixProperties.getModules());
    }

    /**
     * 检查包名是否匹配模式
     *
     * @param packageName 包名
     * @param pattern 模式（支持*通配符）
     * @return 是否匹配
     */
    private boolean matchesPattern(String packageName, String pattern) {
        // 将模式转换为正则表达式
        // 转义点号，将*替换为.*
        String regex = pattern.replace(".", "\\.").replace("*", ".*");
        boolean matches = packageName.matches(regex);
        log.debug("包名匹配检查: {} 匹配模式 {} -> {}", packageName, pattern, matches);
        return matches;
    }
}