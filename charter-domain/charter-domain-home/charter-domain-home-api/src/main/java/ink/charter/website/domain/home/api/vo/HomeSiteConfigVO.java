package ink.charter.website.domain.home.api.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 站点配置VO
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@Schema(description = "站点配置")
public class HomeSiteConfigVO {

    @Schema(description = "配置ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "配置键（唯一标识）")
    private String configKey;

    @Schema(description = "配置值（JSON格式）")
    private String configValue;

    @Schema(description = "配置类型（site/wallpaper/weather/hitokoto/other）")
    private String configType;

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "配置描述")
    private String description;

    @Schema(description = "是否系统配置（0否 1是）")
    private Integer isSystem;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
