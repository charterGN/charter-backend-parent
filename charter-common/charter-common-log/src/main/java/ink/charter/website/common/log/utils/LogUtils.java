package ink.charter.website.common.log.utils;

import ink.charter.website.common.core.utils.JsonUtils;
import ink.charter.website.common.auth.utils.SecurityUtils;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.common.core.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 日志工具类
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
public class LogUtils {


    private static final String UNKNOWN = "unknown";

    /**
     * 获取客户端IP地址
     * 使用IpUtils提供的更完善的IP获取逻辑
     *
     * @param request HTTP请求
     * @return IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        return ip != null ? ip : UNKNOWN;
    }

    /**
     * 获取用户代理信息
     *
     * @param request HTTP请求
     * @return 用户代理信息
     */
    public static String getUserAgent(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        return request.getHeader(LogConstant.HttpHeader.USER_AGENT);
    }

    /**
     * 过滤敏感参数
     *
     * @param params 原始参数
     * @param ignoreParams 需要忽略的参数名
     * @return 过滤后的参数
     */
    public static Map<String, Object> filterSensitiveParams(Map<String, Object> params, String[] ignoreParams) {
        if (params == null || params.isEmpty()) {
            return params;
        }

        Map<String, Object> filteredParams = new HashMap<>(params);
        
        // 收集所有敏感参数
        Set<String> allSensitiveParams = new HashSet<>();
        allSensitiveParams.addAll(Arrays.asList(LogConstant.SensitiveParam.PASSWORD_PARAMS));
        allSensitiveParams.addAll(Arrays.asList(LogConstant.SensitiveParam.TOKEN_PARAMS));
        allSensitiveParams.addAll(Arrays.asList(LogConstant.SensitiveParam.SECRET_PARAMS));
        
        // 添加自定义忽略参数
        if (ignoreParams != null) {
            allSensitiveParams.addAll(Arrays.asList(ignoreParams));
        }
        
        // 过滤敏感参数（不区分大小写）
        for (String key : filteredParams.keySet()) {
            for (String sensitiveParam : allSensitiveParams) {
                if (key.toLowerCase().contains(sensitiveParam.toLowerCase())) {
                    filteredParams.put(key, "***");
                    break;
                }
            }
        }
        
        return filteredParams;
    }

    /**
     * 对象转JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        String result = JsonUtils.toJsonString(obj);
        if (result == null) {
            log.warn("对象转JSON失败");
            return obj.toString();
        }
        return result;
    }

    /**
     * 截断字符串
     *
     * @param str 原字符串
     * @param maxLength 最大长度
     * @return 截断后的字符串
     */
    public static String truncateString(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    /**
     * 获取方法的完整名称
     *
     * @param className 类名
     * @param methodName 方法名
     * @return 完整方法名
     */
    public static String getFullMethodName(String className, String methodName) {
        return className + "." + methodName;
    }

    /**
     * 获取请求URI
     *
     * @param request HTTP请求
     * @return 请求URI
     */
    public static String getRequestUri(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        return request.getRequestURI();
    }

    /**
     * 获取请求方法
     *
     * @param request HTTP请求
     * @return 请求方法
     */
    public static String getRequestMethod(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        return request.getMethod();
    }

    /**
     * 根据IP地址获取地理位置信息
     * 优先使用纯真IP数据库，失败时回退到在线API
     *
     * @param ip IP地址
     * @return 地理位置信息
     */
    public static String getAddressByIp(String ip) {
        if (!StringUtils.hasText(ip) || UNKNOWN.equals(ip)) {
            return "未知地区";
        }
        
        // 使用IpUtils判断是否为内网IP
        if (IpUtils.isInternalIp(ip)) {
            return "内网IP";
        }
        
        // 优先使用纯真IP数据库
        String location = IpUtils.getIpLocation(ip);
        if (!"未知".equals(location)) {
            return location;
        }
        
        // 回退到在线API查询
        return getAddressByOnlineApi(ip);
    }
    
    /**
     * 通过在线API获取IP地理位置信息
     * 
     * @param ip IP地址
     * @return 地理位置信息
     */
    private static String getAddressByOnlineApi(String ip) {
        try {
            // 使用异步方式查询，避免阻塞主线程
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    String apiUrl = "http://whois.pconline.com.cn/ipJson.jsp?ip=" + ip + "&json=true";
                    URL url = new URL(apiUrl);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "GBK"));
                    String line;
                    StringBuilder result = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    
                    String response = result.toString();
                    // 简单解析返回的JSON（实际项目中建议使用JSON库）
                    if (response.contains("pro") && response.contains("city")) {
                        String province = extractJsonValue(response, "pro");
                        String city = extractJsonValue(response, "city");
                        return province + " " + city;
                    }
                    return "未知地区";
                } catch (Exception e) {
                    log.warn("查询IP地址归属地失败: {}", e.getMessage());
                    return "未知地区";
                }
            });
            
            // 设置超时时间，避免长时间等待
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("获取IP地址归属地超时或失败: {}", e.getMessage());
            return "未知地区";
        }
    }

    /**
     * 判断是否为本地IP
     * 使用IpUtils提供的更完善的内网IP判断逻辑
     */
    public static boolean isLocalIp(String ip) {
        return IpUtils.isInternalIp(ip);
    }
    
    /**
     * 获取掩码后的IP地址（用于日志记录等场景）
     * 使用IpUtils提供的IP掩码功能
     *
     * @param ip IP地址
     * @return 掩码后的IP地址
     */
    public static String getMaskedIp(String ip) {
        return IpUtils.maskIp(ip);
    }
    
    /**
     * 验证IP地址格式
     * 使用IpUtils提供的IP验证功能
     *
     * @param ip IP地址
     * @return 是否为有效的IPv4地址
     */
    public static boolean isValidIp(String ip) {
        return IpUtils.isValidIPv4(ip);
    }

    /**
     * 简单的JSON值提取（仅用于特定格式）
     */
    private static String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":\"";
            int startIndex = json.indexOf(searchKey);
            if (startIndex != -1) {
                startIndex += searchKey.length();
                int endIndex = json.indexOf("\"", startIndex);
                if (endIndex != -1) {
                    return json.substring(startIndex, endIndex);
                }
            }
        } catch (Exception e) {
            log.warn("解析JSON值失败: {}", e.getMessage());
        }
        return "";
    }

    /**
     * 获取操作系统信息
     *
     * @param userAgent 用户代理字符串
     * @return 操作系统信息
     */
    public static String getOperatingSystem(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return UNKNOWN;
        }
        
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("windows nt 10")) {
            return "Windows 10";
        } else if (userAgent.contains("windows nt 6.3")) {
            return "Windows 8.1";
        } else if (userAgent.contains("windows nt 6.2")) {
            return "Windows 8";
        } else if (userAgent.contains("windows nt 6.1")) {
            return "Windows 7";
        } else if (userAgent.contains("windows nt 6.0")) {
            return "Windows Vista";
        } else if (userAgent.contains("windows nt 5.1")) {
            return "Windows XP";
        } else if (userAgent.contains("windows")) {
            return "Windows";
        } else if (userAgent.contains("mac os x")) {
            return "Mac OS X";
        } else if (userAgent.contains("mac")) {
            return "Mac";
        } else if (userAgent.contains("linux")) {
            return "Linux";
        } else if (userAgent.contains("android")) {
            return "Android";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
            return "iOS";
        }
        return UNKNOWN;
    }

    /**
     * 获取浏览器信息
     *
     * @param userAgent 用户代理字符串
     * @return 浏览器信息
     */
    public static String getBrowser(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return UNKNOWN;
        }
        
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("edg")) {
            return "Microsoft Edge";
        } else if (userAgent.contains("chrome") && !userAgent.contains("chromium")) {
            return "Google Chrome";
        } else if (userAgent.contains("firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
            return "Safari";
        } else if (userAgent.contains("opera") || userAgent.contains("opr")) {
            return "Opera";
        } else if (userAgent.contains("msie") || userAgent.contains("trident")) {
            return "Internet Explorer";
        }
        return UNKNOWN;
    }

    /**
     * 安全地获取当前登录用户ID
     * 这里提供一个通用的接口，具体实现需要根据项目的认证框架调整
     */
    public static Long getCurrentUserId() {
        try {
            // 使用SecurityUtils获取当前用户ID
            return SecurityUtils.getCurrentUserId();
        } catch (Exception e) {
            log.warn("获取当前用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 安全地获取当前登录用户名
     */
    public static String getCurrentUsername() {
        try {
            // 使用SecurityUtils获取当前用户名
            return SecurityUtils.getCurrentUsername();
        } catch (Exception e) {
            log.warn("获取当前用户名失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取当前HTTP请求
     */
    private static HttpServletRequest getCurrentRequest() {
        try {
            return ((org.springframework.web.context.request.ServletRequestAttributes) 
                    org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }
}