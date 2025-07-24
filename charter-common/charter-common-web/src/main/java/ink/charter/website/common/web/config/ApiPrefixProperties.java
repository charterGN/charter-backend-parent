package ink.charter.website.common.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * API前缀配置属性
 *
 * @author charter
 * @create 2025/01/17
 */
@Data
@ConfigurationProperties(prefix = "charter.web.api")
public class ApiPrefixProperties {

    /**
     * 是否启用API前缀配置
     */
    private boolean enabled = true;

    /**
     * 默认API前缀
     */
    private String defaultPrefix = "/api";

    /**
     * 模块特定的API前缀配置
     * key: 包名模式（支持通配符）
     * value: API前缀
     */
    private Map<String, String> modules = new HashMap<>();

    /**
     * 初始化默认配置
     */
    public ApiPrefixProperties() {
        // 设置默认的模块前缀配置
        modules.put("*.server.admin.controller", "/api-admin");
        modules.put("*.server.home.controller", "/api-home");
        modules.put("*.server.blog.controller", "/api-blog");
    }

    /**
     * 根据包名获取对应的API前缀
     *
     * @param packageName 包名
     * @return API前缀
     */
    public String getPrefixForPackage(String packageName) {
        if (!enabled) {
            return "";
        }

        // 查找匹配的模块配置
        for (Map.Entry<String, String> entry : modules.entrySet()) {
            String pattern = entry.getKey();
            String prefix = entry.getValue();
            
            if (matchesPattern(packageName, pattern)) {
                return prefix;
            }
        }

        // 如果没有匹配的模块配置，返回默认前缀
        return defaultPrefix;
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
        return packageName.matches(regex);
    }
}