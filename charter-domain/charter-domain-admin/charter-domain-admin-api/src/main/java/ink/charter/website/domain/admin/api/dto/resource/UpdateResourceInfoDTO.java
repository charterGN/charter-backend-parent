package ink.charter.website.domain.admin.api.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新资源信息DTO（仅允许更新名称和描述）
 *
 * @author charter
 * @create 2025/11/12
 */
@Data
@Schema(name = "UpdateResourceInfoDTO", description = "更新资源信息请求参数")
public class UpdateResourceInfoDTO implements Serializable {

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
     * 资源描述
     */
    @Schema(description = "资源描述", example = "查询用户信息")
    private String description;
}
