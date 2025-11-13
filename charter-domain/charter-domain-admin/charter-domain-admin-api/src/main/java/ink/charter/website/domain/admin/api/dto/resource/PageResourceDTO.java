package ink.charter.website.domain.admin.api.dto.resource;

import ink.charter.website.common.core.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询资源DTO
 *
 * @author charter
 * @create 2025/11/05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "PageResourceDTO", description = "查询资源请求参数")
public class PageResourceDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
     * 所属模块
     */
    @Schema(description = "所属模块")
    private String module;

    /**
     * 状态（0禁用 1启用）
     */
    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;
}
