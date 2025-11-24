package ink.charter.website.domain.home.api.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 社交链接VO
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@Schema(description = "社交链接")
public class HomeSocialLinkVO {

    @Schema(description = "社交链接ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "平台名称")
    private String platformName;

    @Schema(description = "平台编码")
    private String platformCode;

    @Schema(description = "链接地址")
    private String linkUrl;

    @Schema(description = "图标标识")
    private String icon;

    @Schema(description = "图标URL")
    private String iconUrl;

    @Schema(description = "鼠标悬停提示文本")
    private String hoverTip;

    @Schema(description = "二维码URL")
    private String qrCodeUrl;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
