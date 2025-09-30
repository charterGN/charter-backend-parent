package ink.charter.website.domain.admin.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 修改用户状态请求DTO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Schema(description = "修改用户状态请求")
public class ChangeUserStatusDTO {

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull(message = "用户状态不能为空")
    @Min(value = 0, message = "用户状态值不正确")
    @Max(value = 1, message = "用户状态值不正确")
    @Schema(description = "用户状态：0-禁用，1-启用", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Schema(description = "状态修改原因", example = "违规操作")
    private String reason;
}