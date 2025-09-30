package ink.charter.website.domain.admin.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;

/**
 * 创建用户请求DTO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Schema(description = "创建用户请求")
public class CreateUserDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Schema(description = "密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "admin@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @Schema(description = "昵称", example = "管理员")
    private String nickname;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "头像URL", example = "http://example.com/avatar.jpg")
    private String avatar;

    @NotNull(message = "用户状态不能为空")
    @Min(value = 0, message = "用户状态值不正确")
    @Max(value = 1, message = "用户状态值不正确")
    @Schema(description = "用户状态：0-禁用，1-启用", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}