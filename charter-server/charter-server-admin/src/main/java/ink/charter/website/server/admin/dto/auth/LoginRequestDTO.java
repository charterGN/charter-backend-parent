package ink.charter.website.server.admin.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录请求DTO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Schema(name = "LoginRequestDTO", description = "登录请求数据传输对象")
public class LoginRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名或邮箱
     */
    @NotBlank(message = "用户名或邮箱不能为空")
    @Schema(description = "用户名或邮箱", example = "admin")
    private String usernameOrEmail;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "123456")
    private String password;

    /**
     * 记住我
     */
    @Schema(description = "记住我", example = "false")
    private Boolean rememberMe = false;

    /**
     * 验证码
     */
    @Schema(description = "验证码", example = "1234")
    private String captcha;

    /**
     * 验证码key
     */
    @Schema(description = "验证码key")
    private String captchaKey;
}