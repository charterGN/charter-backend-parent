package ink.charter.website.common.mail.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件配置属性类
 *
 * @author charter
 * @create 2025/11/04
 */
@Data
@Component
@ConfigurationProperties(prefix = "charter.mail")
public class MailProperties {

    /**
     * 是否启用邮件服务
     */
    private boolean enabled = true;

    /**
     * 是否启用异步发送
     */
    private boolean enableAsync = true;

    /**
     * 默认发件人
     */
    private String defaultFrom;

    /**
     * 默认发件人名称
     */
    private String defaultFromName = "Charter Website";

    /**
     * 邮件模板路径前缀
     */
    private String templatePrefix = "mail/";

    /**
     * 邮件模板后缀
     */
    private String templateSuffix = ".html";

    /**
     * 发送失败重试次数
     */
    private int retryTimes = 3;

    /**
     * 重试间隔时间（毫秒）
     */
    private long retryInterval = 1000;
}
