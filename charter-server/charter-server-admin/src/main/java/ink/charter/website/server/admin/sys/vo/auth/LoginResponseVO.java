package ink.charter.website.server.admin.sys.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录响应VO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Accessors(chain = true)
@Schema(name = "LoginResponseVO", description = "登录响应视图对象")
public class LoginResponseVO implements Serializable {

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
     * 过期时间（秒）
     */
    @Schema(description = "访问令牌过期时间（秒）")
    private Long expiresIn;

    /**
     * 用户信息
     */
    @Schema(description = "用户信息")
    private UserInfoVO userInfo;

    /**
     * 创建登录响应
     *
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     * @param userInfo 用户信息
     * @return 登录响应
     */
    public static LoginResponseVO of(String accessToken, String refreshToken, UserInfoVO userInfo) {
        return new LoginResponseVO()
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
    public static LoginResponseVO of(String accessToken, String refreshToken, UserInfoVO userInfo, Long expiresIn) {
        return new LoginResponseVO()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setUserInfo(userInfo)
                .setExpiresIn(expiresIn);
    }
}