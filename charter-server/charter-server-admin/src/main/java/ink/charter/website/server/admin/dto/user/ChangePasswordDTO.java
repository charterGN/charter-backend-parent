package ink.charter.website.server.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 修改密码请求DTO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Schema(description = "修改密码请求")
public class ChangePasswordDTO {

    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotBlank(message = "原密码不能为空")
    @Schema(description = "原密码", example = "oldPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 100, message = "新密码长度必须在6-100个字符之间")
    @Schema(description = "新密码", example = "newPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码", example = "newPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmPassword;

    /**
     * 验证新密码和确认密码是否一致
     */
    public boolean isPasswordMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}