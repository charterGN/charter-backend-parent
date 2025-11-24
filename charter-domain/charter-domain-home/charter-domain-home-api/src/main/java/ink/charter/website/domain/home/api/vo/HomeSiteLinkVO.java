package ink.charter.website.domain.home.api.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 网站链接VO
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@Schema(description = "网站链接")
public class HomeSiteLinkVO {

    @Schema(description = "链接ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "链接名称")
    private String linkName;

    @Schema(description = "链接地址")
    private String linkUrl;

    @Schema(description = "图标标识")
    private String icon;

    @Schema(description = "图标URL")
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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
