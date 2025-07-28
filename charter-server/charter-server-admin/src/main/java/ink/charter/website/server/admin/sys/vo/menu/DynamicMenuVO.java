package ink.charter.website.server.admin.sys.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 动态菜单视图对象
 *
 * @author charter
 * @create 2025/07/28
 */
@Data
@Accessors(chain = true)
@Schema(name = "DynamicMenuVO", description = "动态菜单视图对象")
public class DynamicMenuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    private Long menuId;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @Schema(description = "菜单类型（1目录 2菜单 3按钮）")
    private String menuType;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "是否显示（0隐藏 1显示）")
    private String isHide;

    @Schema(description = "是否外链（0否 1是）")
    private String isLink;

    @Schema(description = "是否缓存（0不缓存 1缓存）")
    private String isKeepAlive;

    @Schema(description = "是否标签显示（0不显示 1显示）")
    private String isTag;

    @Schema(description = "是否固定（0不固定 1固定）")
    private String isAffix;

    @Schema(description = "重定向路径")
    private String redirect;
}
