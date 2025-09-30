package ink.charter.website.domain.admin.api.vo.menu;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单视图对象
 *
 * @author charter
 * @create 2025/07/28
 */
@Data
@Accessors(chain = true)
@Schema(name = "MenuVO", description = "菜单视图对象")
public class MenuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    @JsonSerialize(using = ToStringSerializer.class) // 将Long类型序列化为String，避免前端精度丢失
    private Long menuId;

    @Schema(description = "父菜单ID（0为顶级菜单）")
    @JsonSerialize(using = ToStringSerializer.class) // 将Long类型序列化为String，避免前端精度丢失
    private Long parentId;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "菜单类型（1目录 2菜单 3按钮）")
    private Integer menuType;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    @Schema(description = "是否显示（0隐藏 1显示）")
    private Integer visible;

    @Schema(description = "是否缓存（0不缓存 1缓存）")
    private Integer cache;

    @Schema(description = "是否外链（0否 1是）")
    private Integer externalLink;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}