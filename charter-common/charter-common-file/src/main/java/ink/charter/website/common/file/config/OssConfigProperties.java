package ink.charter.website.common.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OSS 属性类
 *
 * @author charter
 * @create 2025/07/16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "upload.oss")
public class OssConfigProperties {

  private String url;

  private String endpoint;

  private String accessKeyId;

  private String accessKeySecret;

  private String bucketName;
}
