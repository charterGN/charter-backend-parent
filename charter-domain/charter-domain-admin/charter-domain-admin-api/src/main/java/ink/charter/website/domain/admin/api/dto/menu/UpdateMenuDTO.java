package ink.charter.website.domain.admin.api.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新菜单DTO
 *
 * @author Charter
 * @since 2025-01-01
 */
@Data
@Schema(name = "UpdateMenuDTO", description = "更新菜单请求参数")
public class UpdateMenuDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID", example = "1")
    @NotNull(message = "菜单ID不能为空")
    private Long id;

    /**
     * 父菜单ID（0为顶级菜单）
     */
    @Schema(description = "父菜单ID（0为顶级菜单）", example = "0")
    @NotNull(message = "父菜单ID不能为空")
    private Long parentId;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称", example = "系统管理")
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    /**
     * 菜单编码
     */
    @Schema(description = "菜单编码", example = "system")
    private String menuCode;

    /**
     * 菜单类型（1目录 2菜单 3按钮）
     */
    @Schema(description = "菜单类型（1目录 2菜单 3按钮）", example = "1")
    @NotNull(message = "菜单类型不能为空")
    private Integer menuType;

    /**
     * 路由路径
     */
    @Schema(description = "路由路径", example = "/system")
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径", example = "system/index")
    private String component;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标", example = "el-icon-setting")
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    /**
     * 状态（0禁用 1启用）
     */
    @Schema(description = "状态（0禁用 1启用）", example = "1")
    private Integer status;

    /**
     * 是否显示（0隐藏 1显示）
     */
    @Schema(description = "是否显示（0隐藏 1显示）", example = "1")
    private Integer visible;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @Schema(description = "是否缓存（0缓存 1不缓存）", example = "0")
    private Integer cache;

    /**
     * 是否外链（0否 1是）
     */
    @Schema(description = "是否外链（0否 1是）", example = "0")
    private Integer externalLink;
}
