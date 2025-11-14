package ink.charter.website.domain.admin.api.vo.fileconfig;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件配置VO
 *
 * @author charter
 * @create 2025/11/14
 */
@Data
@Schema(name = "FileConfigVO", description = "文件配置信息")
public class FileConfigVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "配置ID")
    @JsonSerialize(using = ToStringSerializer.class) // 将Long类型序列化为String，避免前端精度丢失
    private Long id;

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

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
