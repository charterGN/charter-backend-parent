package ink.charter.website.server.admin.sys.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;

/**
 * 创建字典类型请求DTO
 *
 * @author charter
 * @create 2025/09/15
 */
@Data
@Schema(description = "创建字典类型请求")
public class CreateDictTypeDTO {

    @NotBlank(message = "字典名称不能为空")
    @Size(max = 100, message = "字典名称长度不能超过100个字符")
    @Schema(description = "字典名称", example = "用户状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictName;

    @NotBlank(message = "字典类型不能为空")
    @Size(max = 100, message = "字典类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "字典类型只能包含字母、数字和下划线")
    @Schema(description = "字典类型", example = "sys_user_status", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictType;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不正确")
    @Max(value = 1, message = "状态值不正确")
    @Schema(description = "状态：0-禁用，1-启用", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Schema(description = "备注", example = "用户状态字典")
    private String remark;
}