package ink.charter.website.server.admin.vo.session;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户会话信息VO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Schema(description = "用户会话信息")
public class UserSessionVO {

    @Schema(description = "会话ID", example = "1")
    private Long sessionId;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "会话Token", example = "session_token_123")
    private String sessionToken;

    @Schema(description = "刷新Token", example = "refresh_token_123")
    private String refreshToken;

    @Schema(description = "登录IP", example = "192.168.1.1")
    private String loginIp;

    @Schema(description = "用户代理", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "会话状态：0-无效，1-有效", example = "1")
    private Integer status;

    @Schema(description = "是否已过期", example = "false")
    private Boolean expired;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}