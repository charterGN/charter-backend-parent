package ink.charter.website.domain.admin.api.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建角色DTO
 *
 * @author charter
 * @create 2025/11/05
 */
@Data
@Schema(name = "CreateRoleDTO", description = "创建角色请求参数")
public class CreateRoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "管理员")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码", example = "admin")
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述", example = "系统管理员")
    private String description;

    /**
     * 排序
     */
    @Schema(description = "排序", example = "1")
    private Integer sortOrder = 0;

    /**
     * 状态（0禁用 1启用）
     */
    @Schema(description = "状态（0禁用 1启用）", example = "1")
    private Integer status = 1;
}
