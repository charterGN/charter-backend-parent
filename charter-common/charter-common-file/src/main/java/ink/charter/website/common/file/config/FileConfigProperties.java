package ink.charter.website.common.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传配置属性类
 *
 * @author charter
 * @create 2025/07/16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "charter.file")
public class FileConfigProperties {

  /**
   * 存储类型（oss/minio/huawei/tencent/qiniu）
   */
  private String storageType = "oss";

  /**
   * 访问域名
   */
  private String domain;

  /**
   * 端点地址
   */
  private String endpoint;

  /**
   * 区域
   */
  private String region;

  /**
   * 访问密钥ID
   */
  private String accessKeyId;

  /**
   * 访问密钥Secret
   */
  private String accessKeySecret;

  /**
   * 存储桶名称
   */
  private String bucketName;
}
