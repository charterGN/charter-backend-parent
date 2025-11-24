package ink.charter.website.domain.home.api.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 一言VO
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@Schema(description = "一言")
public class HomeHitokotoVO {

    @Schema(description = "一言ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "一言内容")
    private String hitokoto;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "来源")
    private String fromSource;

    @Schema(description = "作者")
    private String fromWho;

    @Schema(description = "添加者")
    private String creator;

    @Schema(description = "句子长度")
    private Integer length;

    @Schema(description = "UUID")
    private String uuid;

    @Schema(description = "展示次数")
    private Integer viewCount;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
