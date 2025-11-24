package ink.charter.website.common.auth.config;

import java.util.Arrays;

/**
 * 安全白名单配置
 * 统一管理不需要认证的接口路径
 *
 * @author charter
 * @create 2025/11/10
 */
public final class SecurityWhitelistConfig {

    private SecurityWhitelistConfig() {
        // 工具类，禁止实例化
    }

    /**
     * 认证相关接口
     */
    private static final String[] AUTH_WHITELIST = {
            "/api-admin/auth/login",
            "/api-admin/auth/register",
            "/api-admin/auth/captcha",
            "/api-admin/auth/refresh"
    };

    /**
     * 公开接口
     */
    private static final String[] PUBLIC_WHITELIST = {
            "/*/public/**",
            "/api-home/home/**" // 门户相关接口
    };

    /**
     * Swagger文档接口
     */
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    /**
     * 静态资源和系统接口
     */
    private static final String[] SYSTEM_WHITELIST = {
            "/favicon.ico",
            "/error",
            "/actuator/**"
    };

    /**
     * 获取所有白名单路径（用于SecurityConfig）
     *
     * @return 白名单路径数组
     */
    public static String[] getAllWhitelist() {
        return mergeArrays(AUTH_WHITELIST, PUBLIC_WHITELIST, SWAGGER_WHITELIST, SYSTEM_WHITELIST);
    }

    /**
     * 判断路径是否在白名单中（用于JwtAuthenticationFilter）
     *
     * @param path 请求路径
     * @return 是否在白名单中
     */
    public static boolean isWhitelisted(String path) {
        if (path == null) {
            return false;
        }

        String[] allWhitelist = getAllWhitelist();
        for (String pattern : allWhitelist) {
            if (matchesPattern(path, pattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 路径匹配（支持通配符）
     *
     * @param path 请求路径
     * @param pattern 匹配模式
     * @return 是否匹配
     */
    private static boolean matchesPattern(String path, String pattern) {
        // 精确匹配
        if (pattern.equals(path)) {
            return true;
        }

        // 处理 /** 通配符
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(prefix);
        }

        // 处理 /* 通配符
        if (pattern.endsWith("/*")) {
            String prefix = pattern.substring(0, pattern.length() - 2);
            return path.startsWith(prefix) && !path.substring(prefix.length()).contains("/");
        }

        return false;
    }

    /**
     * 合并多个数组
     */
    @SafeVarargs
    private static <T> T[] mergeArrays(T[]... arrays) {
        return Arrays.stream(arrays)
                .flatMap(Arrays::stream)
                .toArray(size -> Arrays.copyOf(arrays[0], size));
    }
}
