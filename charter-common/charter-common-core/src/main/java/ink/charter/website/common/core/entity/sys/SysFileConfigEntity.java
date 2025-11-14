package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文件配置实体类
 *
 * @author charter
 * @create 2025/11/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_file_config")
@Schema(name = "SysFileConfigEntity", description = "文件配置信息")
public class SysFileConfigEntity extends BaseEntity {

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "存储类型（oss/minio/huawei/tencent/qiniu）")
    private String storageType;

    @Schema(description = "访问域名")
    private String domain;

    @Schema(description = "端点地址")
    private String endpoint;

    @Schema(description = "区域")
    private String region;

    @Schema(description = "访问密钥ID")
    private String accessKeyId;

    @Schema(description = "访问密钥Secret")
    private String accessKeySecret;

    @Schema(description = "存储桶名称")
    private String bucketName;

    @Schema(description = "是否启用（0否 1是）")
    private Integer enabled;

    @Schema(description = "备注")
    private String remark;

}
