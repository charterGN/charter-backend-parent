package ink.charter.website.domain.admin.api.dto.role;

import ink.charter.website.common.core.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询角色DTO
 *
 * @author charter
 * @create 2025/11/05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "PageRoleDTO", description = "分页查询角色请求参数")
public class PageRoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分页参数
     */
    @Schema(description = "分页参数")
    private PageRequest pageRequest = new PageRequest();

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
    private String roleCode;

    /**
     * 状态（0禁用 1启用）
     */
    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;
}
