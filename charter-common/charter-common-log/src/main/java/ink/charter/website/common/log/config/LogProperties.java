package ink.charter.website.common.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 日志配置属性类
 *
 * @author charter
 * @create 2025/07/17
 */
@Component
@ConfigurationProperties(prefix = "charter.log")
public class LogProperties {

    /**
     * 是否启用日志
     */
    private boolean enabled = true;

    /**
     * 是否启用IP地址解析
     */
    private boolean enableIpResolve = true;
    
    /**
     * IP地址解析超时时间（毫秒）
     */
    private int ipResolveTimeout = 3000;
    
    /**
     * 是否启用异步日志
     */
    private boolean enableAsync = true;
    
    /**
     * 请求参数最大长度
     */
    private int maxParamLength = 2000;
    
    /**
     * 响应数据最大长度
     */
    private int maxResponseLength = 2000;
    
    /**
     * 错误信息最大长度
     */
    private int maxErrorLength = 500;

    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnableIpResolve() {
        return enableIpResolve;
    }

    public void setEnableIpResolve(boolean enableIpResolve) {
        this.enableIpResolve = enableIpResolve;
    }

    public int getIpResolveTimeout() {
        return ipResolveTimeout;
    }

    public void setIpResolveTimeout(int ipResolveTimeout) {
        this.ipResolveTimeout = ipResolveTimeout;
    }

    public boolean isEnableAsync() {
        return enableAsync;
    }

    public void setEnableAsync(boolean enableAsync) {
        this.enableAsync = enableAsync;
    }

    public int getMaxParamLength() {
        return maxParamLength;
    }

    public void setMaxParamLength(int maxParamLength) {
        this.maxParamLength = maxParamLength;
    }

    public int getMaxResponseLength() {
        return maxResponseLength;
    }

    public void setMaxResponseLength(int maxResponseLength) {
        this.maxResponseLength = maxResponseLength;
    }

    public int getMaxErrorLength() {
        return maxErrorLength;
    }

    public void setMaxErrorLength(int maxErrorLength) {
        this.maxErrorLength = maxErrorLength;
    }
}