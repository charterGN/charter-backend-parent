package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户会话实体类
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_session")
@Schema(name = "SysUserSessionEntity", description = "用户会话")
public class SysUserSessionEntity extends BaseEntity {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "会话标识")
    private String sessionId;

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "登录地址")
    private String loginAddress;

    @Schema(description = "登录IP")
    private String loginIp;

    @Schema(description = "用户代理")
    private String userAgent;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "状态（0离线 1在线）")
    private Integer status;
}