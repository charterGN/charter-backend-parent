package ink.charter.website.common.log.constant;

/**
 * 日志常量类
 * 定义日志相关的常量
 *
 * @author charter
 * @create 2025/07/17
 */
public class LogConstant {

    /**
     * 操作状态
     */
    public static class OptStatus {
        /** 操作失败 */
        public static final Integer FAIL = 1;
        /** 操作成功 */
        public static final Integer SUCCESS = 0;
    }

    /**
     * 操作类型
     */
    public static class OptType {
        /** 查询 */
        public static final String SELECT = "查询";
        /** 新增 */
        public static final String INSERT = "新增";
        /** 修改 */
        public static final String UPDATE = "修改";
        /** 删除 */
        public static final String DELETE = "删除";
        /** 导入 */
        public static final String IMPORT = "导入";
        /** 导出 */
        public static final String EXPORT = "导出";
        /** 登录 */
        public static final String LOGIN = "登录";
        /** 登出 */
        public static final String LOGOUT = "登出";
        /** 授权 */
        public static final String GRANT = "授权";
        /** 执行 */
        public static final String EXECUTE = "执行";
        /** 其他 */
        public static final String OTHER = "其他";
    }

    /**
     * 操作模块
     */
    public static class OptModule {
        /** auth操作 */
        public static final String AUTH = "auth操作";
        /** 用户管理 */
        public static final String USER = "用户管理";
        /** 角色管理 */
        public static final String ROLE = "角色管理";
        /** 菜单管理 */
        public static final String MENU = "菜单管理";
        /** 资源管理 */
        public static final String RESOURCE = "资源管理";
        /** 系统管理 */
        public static final String SYSTEM = "系统管理";
        /** 日志管理 */
        public static final String LOG = "日志管理";
        /** 定时任务管理 */
        public static final String JOB = "定时任务管理";
        /** 其他 */
        public static final String OTHER = "其他";
    }

    /**
     * 敏感参数名称
     */
    public static class SensitiveParam {
        /** 密码相关 */
        public static final String[] PASSWORD_PARAMS = {
            "password", "pwd", "pass", "passwd", "newPassword", "oldPassword", "confirmPassword"
        };
        
        /** 令牌相关 */
        public static final String[] TOKEN_PARAMS = {
            "token", "accessToken", "refreshToken", "authorization", "auth"
        };
        
        /** 密钥相关 */
        public static final String[] SECRET_PARAMS = {
            "secret", "key", "privateKey", "publicKey", "secretKey", "apiKey"
        };
    }

    /**
     * HTTP请求头
     */
    public static class HttpHeader {
        /** 用户代理 */
        public static final String USER_AGENT = "User-Agent";
        /** 真实IP */
        public static final String X_REAL_IP = "X-Real-IP";
        /** 转发IP */
        public static final String X_FORWARDED_FOR = "X-Forwarded-For";
        /** 代理客户端IP */
        public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
        /** WL代理客户端IP */
        public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
        /** HTTP客户端IP */
        public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
        /** HTTP转发IP */
        public static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    }

    /**
     * IP地址相关
     */
    public static class IpAddress {
        /** 本地IP */
        public static final String LOCALHOST_IPV4 = "127.0.0.1";
        /** 本地IPv6 */
        public static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
        /** 未知地区 */
        public static final String UNKNOWN_REGION = "未知地区";
        /** 内网IP */
        public static final String INTRANET = "内网IP";
    }

    /**
     * 字符串长度限制
     */
    public static class StringLength {
        /** 请求参数最大长度 */
        public static final int MAX_PARAM_LENGTH = 2000;
        /** 响应数据最大长度 */
        public static final int MAX_RESPONSE_LENGTH = 2000;
        /** 错误信息最大长度 */
        public static final int MAX_ERROR_LENGTH = 500;
        /** 描述信息最大长度 */
        public static final int MAX_DESC_LENGTH = 200;
        /** URL最大长度 */
        public static final int MAX_URL_LENGTH = 500;
    }

    /**
     * 线程池配置
     */
    public static class ThreadPool {
        /** 核心线程数 */
        public static final int CORE_POOL_SIZE = 2;
        /** 最大线程数 */
        public static final int MAX_POOL_SIZE = 5;
        /** 队列容量 */
        public static final int QUEUE_CAPACITY = 100;
        /** 线程空闲时间（秒） */
        public static final int KEEP_ALIVE_SECONDS = 60;
        /** 等待终止时间（秒） */
        public static final int AWAIT_TERMINATION_SECONDS = 60;
    }

    /**
     * 超时配置
     */
    public static class Timeout {
        /** IP地址解析超时时间（毫秒） */
        public static final int IP_RESOLVE_TIMEOUT = 3000;
        /** HTTP连接超时时间（毫秒） */
        public static final int HTTP_CONNECT_TIMEOUT = 3000;
        /** HTTP读取超时时间（毫秒） */
        public static final int HTTP_READ_TIMEOUT = 5000;
    }
}