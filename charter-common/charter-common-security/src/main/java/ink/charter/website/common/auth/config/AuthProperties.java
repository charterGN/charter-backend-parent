package ink.charter.website.common.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 认证相关配置属性
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Component
@ConfigurationProperties(prefix = "charter.auth")
public class AuthProperties {

    /**
     * 是否启用认证
     */
    private Boolean enabled = true;

    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * 登录配置
     */
    private Login login = new Login();

    /**
     * 加密配置
     */
    private Crypto crypto = new Crypto();

    /**
     * 会话配置
     */
    private Session session = new Session();

    @Data
    public static class Jwt {
        /**
         * JWT密钥
         */
        private String secret = "charter-website-jwt-secret-key-2025";

        /**
         * 访问令牌过期时间（秒）
         */
        private Long accessTokenExpire = 7200L; // 2小时

        /**
         * 刷新令牌过期时间（秒）
         */
        private Long refreshTokenExpire = 604800L; // 7天

        /**
         * 令牌发行者
         */
        private String issuer = "charter-website";
    }

    @Data
    public static class Login {
        /**
         * 最大登录尝试次数
         */
        private Integer maxAttempts = 5;

        /**
         * 锁定时间（秒）
         */
        private Long lockTime = 1800L; // 30分钟

        /**
         * 是否启用验证码
         */
        private Boolean enableCaptcha = true;

        /**
         * 验证码过期时间（秒）
         */
        private Long captchaExpire = 300L; // 5分钟
    }

    @Data
    public static class Session {
        /**
         * 是否单点登录
         */
        private Boolean singleLogin = true;

        /**
         * 最大会话数
         */
        private Integer maxSessions = 1;

        /**
         * 会话超时检查间隔（秒）
         */
        private Long timeoutCheckInterval = 300L; // 5分钟
    }

    @Data
    public static class Crypto {
        /**
         * 密码解密密钥
         */
        private String secretKey = "secretCharterKey";
    }
}