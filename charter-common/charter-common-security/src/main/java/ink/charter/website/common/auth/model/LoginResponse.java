package ink.charter.website.common.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录响应数据传输对象
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Accessors(chain = true)
@Schema(name = "LoginResponse", description = "登录响应")
public class LoginResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 访问令牌
     */
    @Schema(description = "访问令牌")
    private String accessToken;

    /**
     * 刷新令牌
     */
    @Schema(description = "刷新令牌")
    private String refreshToken;

    /**
     * 用户信息
     */
    @Schema(description = "用户信息")
    private LoginUser userInfo;

    /**
     * 令牌过期时间（秒）
     */
    @Schema(description = "令牌过期时间（秒）")
    private Long expiresIn;

    /**
     * 创建登录响应
     *
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     * @param userInfo 用户信息
     * @return 登录响应
     */
    public static LoginResponse of(String accessToken, String refreshToken, LoginUser userInfo) {
        return new LoginResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setUserInfo(userInfo);
    }

    /**
     * 创建登录响应
     *
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     * @param userInfo 用户信息
     * @param expiresIn 过期时间
     * @return 登录响应
     */
    public static LoginResponse of(String accessToken, String refreshToken, LoginUser userInfo, Long expiresIn) {
        return new LoginResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setUserInfo(userInfo)
                .setExpiresIn(expiresIn);
    }
}