package ink.charter.website.domain.admin.api.vo.dict;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典数据响应VO
 *
 * @author charter
 * @create 2025/09/15
 */
@Data
@Schema(description = "字典数据响应")
public class DictDataVO {

    @Schema(description = "字典数据ID", example = "1")
    @JsonSerialize(using = ToStringSerializer.class) // 将Long类型序列化为String，避免前端精度丢失
    private Long id;

    @Schema(description = "字典类型", example = "sys_user_status")
    private String dictType;

    @Schema(description = "字典标签", example = "启用")
    private String dictLabel;

    @Schema(description = "字典键值", example = "1")
    private String dictValue;

    @Schema(description = "标签类型", example = "success")
    private String dictTag;

    @Schema(description = "标签颜色", example = "#67C23A")
    private String dictColor;

    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;

    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    @Schema(description = "备注", example = "用户启用状态")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}