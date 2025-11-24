package ink.charter.website.domain.home.api.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 壁纸VO
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@Schema(description = "壁纸")
public class HomeWallpaperVO {

    @Schema(description = "壁纸ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "壁纸名称")
    private String wallpaperName;

    @Schema(description = "壁纸类型（0默认 1每日一图 2随机风景 3随机动漫）")
    private Integer wallpaperType;

    @Schema(description = "壁纸URL")
    private String fileUrl;

    @Schema(description = "缩略图URL")
    private String thumbnailUrl;

    @Schema(description = "壁纸来源")
    private String fromSource;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "壁纸描述")
    private String description;

    @Schema(description = "标签")
    private String tags;

    @Schema(description = "图片宽度")
    private Integer width;

    @Schema(description = "图片高度")
    private Integer height;

    @Schema(description = "文件大小（字节）")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileSize;

    @Schema(description = "是否默认壁纸（0否 1是）")
    private Integer isDefault;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
