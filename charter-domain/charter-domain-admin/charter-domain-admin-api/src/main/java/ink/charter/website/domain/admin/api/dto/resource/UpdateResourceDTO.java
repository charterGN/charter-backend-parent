package ink.charter.website.domain.admin.api.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新资源DTO
 *
 * @author charter
 * @create 2025/11/05
 */
@Data
@Schema(name = "UpdateResourceDTO", description = "更新资源请求参数")
public class UpdateResourceDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    @Schema(description = "资源ID", example = "1")
    @NotNull(message = "资源ID不能为空")
    private Long id;

    /**
     * 资源名称
     */
    @Schema(description = "资源名称", example = "用户查询")
    @NotBlank(message = "资源名称不能为空")
    private String resourceName;

    /**
     * 资源编码
     */
    @Schema(description = "资源编码", example = "user:query")
    @NotBlank(message = "资源编码不能为空")
    private String resourceCode;

    /**
     * 资源类型（1接口 2文件 3数据）
     */
    @Schema(description = "资源类型（1接口 2文件 3数据）", example = "1")
    @NotNull(message = "资源类型不能为空")
    private Integer resourceType;

    /**
     * 资源URL
     */
    @Schema(description = "资源URL", example = "/api/user/query")
    private String url;

    /**
     * HTTP方法
     */
    @Schema(description = "HTTP方法", example = "GET")
    private String method;

    /**
     * 资源描述
     */
    @Schema(description = "资源描述", example = "查询用户信息")
    private String description;

    /**
     * 状态（0禁用 1启用）
     */
    @Schema(description = "状态（0禁用 1启用）", example = "1")
    private Integer status;
}
