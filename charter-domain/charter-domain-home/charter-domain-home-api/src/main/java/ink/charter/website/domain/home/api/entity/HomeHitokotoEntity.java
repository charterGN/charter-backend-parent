package ink.charter.website.domain.home.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 门户一言库实体类
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("home_hitokoto")
@Schema(name = "HomeHitokotoEntity", description = "门户一言库")
public class HomeHitokotoEntity extends BaseEntity {

    @Schema(description = "一言内容")
    private String hitokoto;

    @Schema(description = "类型（a动画/b漫画/c游戏/d文学/e原创/f网络/g其他/h影视/i诗词/j网易云/k哲学/l抖机灵）")
    private String type;

    @Schema(description = "来源")
    private String fromSource;

    @Schema(description = "作者")
    private String fromWho;

    @Schema(description = "添加者")
    private String creator;

    @Schema(description = "句子长度")
    private Integer length;

    @Schema(description = "UUID（对接Hitokoto API）")
    private String uuid;

    @Schema(description = "展示次数")
    private Integer viewCount;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;
}
