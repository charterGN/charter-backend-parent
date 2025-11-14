package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典数据实体类
 *
 * @author charter
 * @create 2025/09/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dict_data")
@Schema(name = "SysDictDataEntity", description = "字典数据")
public class SysDictDataEntity extends BaseEntity {

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "字典键值")
    private String dictValue;

    @Schema(description = "标签类型")
    private String dictTag;

    @Schema(description = "标签颜色")
    @TableField(updateStrategy = FieldStrategy.ALWAYS) // 允许更新为null
    private String dictColor;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "备注")
    private String remark;
}