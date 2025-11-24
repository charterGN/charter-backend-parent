package ink.charter.website.domain.home.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 门户网站链接实体类
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("home_site_link")
@Schema(name = "HomeSiteLinkEntity", description = "门户网站链接")
public class HomeSiteLinkEntity extends BaseEntity {

    @Schema(description = "链接名称")
    private String linkName;

    @Schema(description = "链接地址")
    private String linkUrl;

    @Schema(description = "图标标识（对应前端图标组件名）")
    private String icon;

    @Schema(description = "图标URL（自定义图标）")
    private String iconUrl;

    @Schema(description = "链接描述")
    private String description;

    @Schema(description = "是否外链（0否 1是）")
    private Integer isExternal;

    @Schema(description = "打开方式（0当前窗口 1新窗口）")
    private Integer openType;

    @Schema(description = "点击次数")
    private Integer clickCount;

    @Schema(description = "状态（0禁用 1启用 2开发中）")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;
}
