package ink.charter.website.domain.admin.api.vo.auth;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户信息VO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Accessors(chain = true)
@Schema(name = "UserInfoVO", description = "用户信息视图对象")
public class UserInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @JsonSerialize(using = ToStringSerializer.class) // 将Long类型序列化为String，避免前端精度丢失
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 头像
     */
    @Schema(description = "头像URL")
    private String avatar;

    /**
     * 状态
     */
    @Schema(description = "用户状态")
    private Integer status;

    /**
     * 角色列表
     */
    @Schema(description = "用户角色列表")
    private Set<String> roles;

    /**
     * 权限列表
     */
    @Schema(description = "用户权限列表")
    private Set<String> permissions;

    /**
     * 登录时间
     */
    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    /**
     * 登录IP
     */
    @Schema(description = "登录IP")
    private String loginIp;

    /**
     * 用户代理
     */
    @Schema(description = "用户代理")
    private String userAgent;
}