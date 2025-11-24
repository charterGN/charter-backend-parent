package ink.charter.website.domain.home.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 门户壁纸管理实体类
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("home_wallpaper")
@Schema(name = "HomeWallpaperEntity", description = "门户壁纸管理")
public class HomeWallpaperEntity extends BaseEntity {

    @Schema(description = "壁纸名称")
    private String wallpaperName;

    @Schema(description = "壁纸类型（0默认 1每日一图 2随机风景 3随机动漫）")
    private Integer wallpaperType;

    @Schema(description = "关联文件ID（sys_files表）")
    private Long fileId;

    @Schema(description = "壁纸URL（外链或本地路径）")
    private String fileUrl;

    @Schema(description = "缩略图URL")
    private String thumbnailUrl;

    @Schema(description = "壁纸来源（bing/unsplash/pixiv等）")
    private String fromSource;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "壁纸描述")
    private String description;

    @Schema(description = "标签（逗号分隔）")
    private String tags;

    @Schema(description = "图片宽度")
    private Integer width;

    @Schema(description = "图片高度")
    private Integer height;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "是否默认壁纸（0否 1是）")
    private Integer isDefault;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;
}
