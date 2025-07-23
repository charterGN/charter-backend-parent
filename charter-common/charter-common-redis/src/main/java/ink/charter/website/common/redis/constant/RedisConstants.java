package ink.charter.website.common.redis.constant;

/**
 * Redis常量类
 * 定义Redis相关的常量
 *
 * @author charter
 * @create 2025/07/19
 */
public class RedisConstants {

    /**
     * 默认过期时间（秒）
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24; // 24小时

    /**
     * 短期过期时间（秒）
     */
    public static final long SHORT_EXPIRE = 60 * 30; // 30分钟

    /**
     * 长期过期时间（秒）
     */
    public static final long LONG_EXPIRE = 60 * 60 * 24 * 7; // 7天

    /**
     * 永不过期
     */
    public static final long NEVER_EXPIRE = -1;

    /**
     * Redis键分隔符
     */
    public static final String KEY_SEPARATOR = ":";

    /**
     * 系统缓存键前缀
     */
    public static final String SYSTEM_CACHE_PREFIX = "system" + KEY_SEPARATOR;

    /**
     * 用户缓存键前缀
     */
    public static final String USER_CACHE_PREFIX = "user" + KEY_SEPARATOR;

    /**
     * 会话缓存键前缀
     */
    public static final String SESSION_CACHE_PREFIX = "session" + KEY_SEPARATOR;

    /**
     * 验证码缓存键前缀
     */
    public static final String CAPTCHA_CACHE_PREFIX = "captcha" + KEY_SEPARATOR;

    /**
     * 令牌缓存键前缀
     */
    public static final String TOKEN_CACHE_PREFIX = "token" + KEY_SEPARATOR;

    /**
     * 权限缓存键前缀
     */
    public static final String PERMISSION_CACHE_PREFIX = "permission" + KEY_SEPARATOR;

    /**
     * 配置缓存键前缀
     */
    public static final String CONFIG_CACHE_PREFIX = "config" + KEY_SEPARATOR;

    /**
     * 字典缓存键前缀
     */
    public static final String DICT_CACHE_PREFIX = "dict" + KEY_SEPARATOR;

    /**
     * 分布式锁键前缀
     */
    public static final String LOCK_PREFIX = "lock" + KEY_SEPARATOR;

    /**
     * 限流键前缀
     */
    public static final String RATE_LIMIT_PREFIX = "rate_limit" + KEY_SEPARATOR;

    /**
     * 构建缓存键
     *
     * @param prefix 前缀
     * @param keys   键值
     * @return 完整的缓存键
     */
    public static String buildKey(String prefix, String... keys) {
        StringBuilder sb = new StringBuilder(prefix);
        for (String key : keys) {
            sb.append(key).append(KEY_SEPARATOR);
        }
        // 移除最后一个分隔符
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == KEY_SEPARATOR.charAt(0)) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}