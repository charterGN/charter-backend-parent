package ink.charter.website.domain.admin.api.vo.dict;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典类型响应VO
 *
 * @author charter
 * @create 2025/09/15
 */
@Data
@Schema(description = "字典类型响应")
public class DictTypeVO {

    @Schema(description = "字典类型ID", example = "1")
    @JsonSerialize(using = ToStringSerializer.class) // 将Long类型序列化为String，避免前端精度丢失
    private Long id;

    @Schema(description = "字典名称", example = "用户状态")
    private String dictName;

    @Schema(description = "字典类型", example = "sys_user_status")
    private String dictType;

    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "用户状态字典")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}