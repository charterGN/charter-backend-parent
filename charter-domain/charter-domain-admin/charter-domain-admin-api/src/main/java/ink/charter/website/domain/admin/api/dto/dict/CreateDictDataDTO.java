package ink.charter.website.domain.admin.api.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;

/**
 * 创建字典数据请求DTO
 *
 * @author charter
 * @create 2025/09/15
 */
@Data
@Schema(description = "创建字典数据请求")
public class CreateDictDataDTO {

    @NotBlank(message = "字典类型不能为空")
    @Size(max = 100, message = "字典类型长度不能超过100个字符")
    @Schema(description = "字典类型", example = "sys_user_status", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictType;

    @NotBlank(message = "字典标签不能为空")
    @Size(max = 100, message = "字典标签长度不能超过100个字符")
    @Schema(description = "字典标签", example = "启用", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictLabel;

    @NotBlank(message = "字典键值不能为空")
    @Size(max = 100, message = "字典键值长度不能超过100个字符")
    @Schema(description = "字典键值", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictValue;

    @Size(max = 50, message = "标签类型长度不能超过50个字符")
    @Schema(description = "标签类型", example = "success")
    private String dictTag;

    @Size(max = 50, message = "标签颜色长度不能超过50个字符")
    @Schema(description = "标签颜色", example = "#67C23A")
    private String dictColor;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不正确")
    @Max(value = 1, message = "状态值不正确")
    @Schema(description = "状态：0-禁用，1-启用", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Min(value = 0, message = "排序值不能小于0")
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Schema(description = "备注", example = "用户启用状态")
    private String remark;
}