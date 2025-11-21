package ink.charter.website.common.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 日志配置属性类
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
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
}