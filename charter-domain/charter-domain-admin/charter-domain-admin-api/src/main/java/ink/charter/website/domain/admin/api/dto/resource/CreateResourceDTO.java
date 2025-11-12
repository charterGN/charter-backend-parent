package ink.charter.website.domain.admin.api.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建资源DTO
 *
 * @author charter
 * @create 2025/11/05
 */
@Data
@Schema(name = "CreateResourceDTO", description = "创建资源请求参数")
public class CreateResourceDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
     * 所属模块
     */
    @Schema(description = "所属模块", example = "用户管理")
    @NotBlank(message = "所属模块不能为空")
    private String module;

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
    private Integer status = 1;
}
