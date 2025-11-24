package ink.charter.website.domain.home.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 门户社交链接实体类
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("home_social_link")
@Schema(name = "HomeSocialLinkEntity", description = "门户社交链接")
public class HomeSocialLinkEntity extends BaseEntity {

    @Schema(description = "平台名称（Github/QQ/微信等）")
    private String platformName;

    @Schema(description = "平台编码（github/qq/wechat等）")
    private String platformCode;

    @Schema(description = "链接地址")
    private String linkUrl;

    @Schema(description = "图标标识（对应前端图标组件名）")
    private String icon;

    @Schema(description = "图标URL（自定义图标）")
    private String iconUrl;

    @Schema(description = "鼠标悬停提示文本")
    private String hoverTip;

    @Schema(description = "二维码URL（如微信/QQ）")
    private String qrCodeUrl;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;
}
