package ink.charter.website.domain.admin.api.vo.resource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源VO
 *
 * @author charter
 * @create 2025/11/05
 */
@Data
@Schema(name = "ResourceVO", description = "资源信息")
public class ResourceVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    @Schema(description = "资源ID")
    @JsonSerialize(using = ToStringSerializer.class) // 将Long类型序列化为String，避免前端精度丢失
    private Long id;

    /**
     * 资源名称
     */
    @Schema(description = "资源名称")
    private String resourceName;

    /**
     * 资源编码
     */
    @Schema(description = "资源编码")
    private String resourceCode;

    /**
     * 资源类型（1接口 2文件 3数据）
     */
    @Schema(description = "资源类型（1接口 2文件 3数据）")
    private Integer resourceType;

    /**
     * 资源URL
     */
    @Schema(description = "资源URL")
    private String url;

    /**
     * HTTP方法
     */
    @Schema(description = "HTTP方法")
    private String method;

    /**
     * 资源描述
     */
    @Schema(description = "资源描述")
    private String description;

    /**
     * 状态（0禁用 1启用）
     */
    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
