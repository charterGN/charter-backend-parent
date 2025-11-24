package ink.charter.website.domain.home.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 门户站点配置实体类
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("home_site_config")
@Schema(name = "HomeSiteConfigEntity", description = "门户站点配置")
public class HomeSiteConfigEntity extends BaseEntity {

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

    @Schema(description = "是否系统配置（0否 1是，系统配置不可删除）")
    private Integer isSystem;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;
}
