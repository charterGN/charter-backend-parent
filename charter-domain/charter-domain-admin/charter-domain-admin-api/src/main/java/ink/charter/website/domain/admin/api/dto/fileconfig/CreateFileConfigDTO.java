package ink.charter.website.domain.admin.api.dto.fileconfig;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建文件配置DTO
 *
 * @author charter
 * @create 2025/11/14
 */
@Data
@Schema(name = "CreateFileConfigDTO", description = "创建文件配置请求")
public class CreateFileConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "配置名称")
    @NotBlank(message = "配置名称不能为空")
    private String configName;

    @Schema(description = "存储类型（oss/minio/huawei/tencent/qiniu）")
    @NotBlank(message = "存储类型不能为空")
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
    @NotNull(message = "启用状态不能为空")
    private Integer enabled;

    @Schema(description = "备注")
    private String remark;

}
