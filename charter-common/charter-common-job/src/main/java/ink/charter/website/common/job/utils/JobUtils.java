package ink.charter.website.common.job.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 任务工具类
 * 提供任务执行过程中的通用工具方法
 *
 * @author charter
 * @create 2025/07/30
 */
@Slf4j
public class JobUtils {
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    /**
     * 解析JSON参数为Map
     *
     * @param jsonParams JSON格式的参数字符串
     * @return 参数Map
     */
    public static Map<String, Object> parseParams(String jsonParams) {
        if (jsonParams == null || jsonParams.trim().isEmpty()) {
            return new HashMap<>();
        }
        
        try {
            return OBJECT_MAPPER.readValue(jsonParams, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.warn("解析任务参数失败: {}, 错误: {}", jsonParams, e.getMessage());
            return new HashMap<>();
        }
    }
    
    /**
     * 将Map转换为JSON字符串
     *
     * @param params 参数Map
     * @return JSON字符串
     */
    public static String toJsonParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "{}";
        }
        
        try {
            return OBJECT_MAPPER.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            log.warn("转换任务参数失败: {}, 错误: {}", params, e.getMessage());
            return "{}";
        }
    }
    
    /**
     * 从参数Map中获取字符串值
     *
     * @param params 参数Map
     * @param key 参数键
     * @return 字符串值
     */
    public static String getStringParam(Map<String, Object> params, String key) {
        return getStringParam(params, key, null);
    }
    
    /**
     * 从参数Map中获取字符串值
     *
     * @param params 参数Map
     * @param key 参数键
     * @param defaultValue 默认值
     * @return 字符串值
     */
    public static String getStringParam(Map<String, Object> params, String key, String defaultValue) {
        if (params == null || key == null) {
            return defaultValue;
        }
        
        Object value = params.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * 从参数Map中获取整数值
     *
     * @param params 参数Map
     * @param key 参数键
     * @return 整数值
     */
    public static Integer getIntParam(Map<String, Object> params, String key) {
        return getIntParam(params, key, null);
    }
    
    /**
     * 从参数Map中获取整数值
     *
     * @param params 参数Map
     * @param key 参数键
     * @param defaultValue 默认值
     * @return 整数值
     */
    public static Integer getIntParam(Map<String, Object> params, String key, Integer defaultValue) {
        if (params == null || key == null) {
            return defaultValue;
        }
        
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        try {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            log.warn("参数 [{}] 转换为整数失败: {}", key, value);
            return defaultValue;
        }
    }
    
    /**
     * 从参数Map中获取长整数值
     *
     * @param params 参数Map
     * @param key 参数键
     * @return 长整数值
     */
    public static Long getLongParam(Map<String, Object> params, String key) {
        return getLongParam(params, key, null);
    }
    
    /**
     * 从参数Map中获取长整数值
     *
     * @param params 参数Map
     * @param key 参数键
     * @param defaultValue 默认值
     * @return 长整数值
     */
    public static Long getLongParam(Map<String, Object> params, String key, Long defaultValue) {
        if (params == null || key == null) {
            return defaultValue;
        }
        
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            log.warn("参数 [{}] 转换为长整数失败: {}", key, value);
            return defaultValue;
        }
    }
    
    /**
     * 从参数Map中获取布尔值
     *
     * @param params 参数Map
     * @param key 参数键
     * @return 布尔值
     */
    public static Boolean getBooleanParam(Map<String, Object> params, String key) {
        return getBooleanParam(params, key, null);
    }
    
    /**
     * 从参数Map中获取布尔值
     *
     * @param params 参数Map
     * @param key 参数键
     * @param defaultValue 默认值
     * @return 布尔值
     */
    public static Boolean getBooleanParam(Map<String, Object> params, String key, Boolean defaultValue) {
        if (params == null || key == null) {
            return defaultValue;
        }
        
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        
        String strValue = value.toString().toLowerCase();
        return "true".equals(strValue) || "1".equals(strValue) || "yes".equals(strValue);
    }
    
    /**
     * 获取服务器IP地址
     *
     * @return IP地址
     */
    public static String getServerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("获取服务器IP失败: {}", e.getMessage());
            return "unknown";
        }
    }
    
    /**
     * 生成唯一的执行ID
     *
     * @return 执行ID
     */
    public static String generateExecuteId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 格式化执行时间
     *
     * @param costTime 耗时（毫秒）
     * @return 格式化的时间字符串
     */
    public static String formatCostTime(long costTime) {
        if (costTime < 1000) {
            return costTime + "ms";
        } else if (costTime < 60000) {
            return String.format("%.2fs", costTime / 1000.0);
        } else if (costTime < 3600000) {
            long minutes = costTime / 60000;
            long seconds = (costTime % 60000) / 1000;
            return String.format("%dm%ds", minutes, seconds);
        } else {
            long hours = costTime / 3600000;
            long minutes = (costTime % 3600000) / 60000;
            long seconds = (costTime % 60000) / 1000;
            return String.format("%dh%dm%ds", hours, minutes, seconds);
        }
    }
    
    /**
     * 检查字符串是否为空
     *
     * @param str 字符串
     * @return true-为空，false-不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 检查字符串是否不为空
     *
     * @param str 字符串
     * @return true-不为空，false-为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 安全地截取字符串
     *
     * @param str 原字符串
     * @param maxLength 最大长度
     * @return 截取后的字符串
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        
        if (str.length() <= maxLength) {
            return str;
        }
        
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * 构建任务的唯一标识
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @return 唯一标识
     */
    public static String buildJobKey(String jobName, String jobGroup) {
        return jobGroup + ":" + jobName;
    }
    
    /**
     * 解析任务的唯一标识
     *
     * @param jobKey 任务唯一标识
     * @return [jobGroup, jobName]
     */
    public static String[] parseJobKey(String jobKey) {
        if (isEmpty(jobKey)) {
            throw new IllegalArgumentException("任务标识不能为空");
        }
        
        String[] parts = jobKey.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("无效的任务标识格式: " + jobKey);
        }
        
        return parts;
    }
}