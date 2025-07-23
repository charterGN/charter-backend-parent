package ink.charter.website.server.admin.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 刷新Token请求DTO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Schema(name = "RefreshTokenRequestDTO", description = "刷新Token请求数据传输对象")
public class RefreshTokenRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 刷新Token
     */
    @NotBlank(message = "刷新Token不能为空")
    @Schema(description = "刷新Token", required = true)
    private String refreshToken;
}