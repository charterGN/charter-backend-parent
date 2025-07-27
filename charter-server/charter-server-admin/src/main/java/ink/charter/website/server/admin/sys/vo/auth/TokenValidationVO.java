package ink.charter.website.server.admin.sys.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Token验证响应VO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Accessors(chain = true)
@Schema(name = "TokenValidationVO", description = "Token验证响应视图对象")
public class TokenValidationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Token是否有效
     */
    @Schema(description = "Token是否有效")
    private Boolean valid;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * Token过期时间
     */
    @Schema(description = "Token过期时间")
    private LocalDateTime expireTime;

    /**
     * 是否即将过期
     */
    @Schema(description = "是否即将过期")
    private Boolean expiringSoon;

    /**
     * 创建有效的Token验证响应
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param expireTime 过期时间
     * @param expiringSoon 是否即将过期
     * @return Token验证响应
     */
    public static TokenValidationVO valid(Long userId, String username, LocalDateTime expireTime, Boolean expiringSoon) {
        return new TokenValidationVO()
                .setValid(true)
                .setUserId(userId)
                .setUsername(username)
                .setExpireTime(expireTime)
                .setExpiringSoon(expiringSoon);
    }

    /**
     * 创建无效的Token验证响应
     *
     * @return Token验证响应
     */
    public static TokenValidationVO invalid() {
        return new TokenValidationVO()
                .setValid(false);
    }
}