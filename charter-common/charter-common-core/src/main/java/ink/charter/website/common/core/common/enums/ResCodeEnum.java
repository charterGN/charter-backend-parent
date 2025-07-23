package ink.charter.website.common.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 * 统一管理系统中的响应状态码
 *
 * @author charter
 * @create 2025/07/17
 */
@Getter
@AllArgsConstructor
public enum ResCodeEnum {

    // ========== 通用状态码 ==========
    SUCCESS("00000", "操作成功"),
    SYSTEM_ERROR("10000", "系统异常"),
    PARAM_ERROR("10001", "参数错误"),
    PARAM_MISSING("10002", "参数缺失"),
    PARAM_INVALID("10003", "参数格式错误"),
    REQUEST_METHOD_NOT_SUPPORTED("10004", "请求方法不支持"),
    REQUEST_TIMEOUT("10005", "请求超时"),
    RATE_LIMIT_EXCEEDED("10006", "请求频率超限"),
    
    // ========== 认证授权状态码 ==========
    UNAUTHORIZED("20000", "未认证"),
    TOKEN_INVALID("20001", "Token无效"),
    TOKEN_EXPIRED("20002", "Token已过期"),
    PERMISSION_DENIED("20003", "权限不足"),
    LOGIN_FAILED("20004", "登录失败"),
    ACCOUNT_LOCKED("20005", "账户已锁定"),
    ACCOUNT_DISABLED("20006", "账户已禁用"),
    ACCOUNT_EXPIRED("20007", "账户已过期"),
    LOGIN_REQUIRED("20008", "认证失败，请先登录"),
    
    // ========== 业务状态码 ==========
    BUSINESS_ERROR("30000", "业务异常"),
    DATA_NOT_FOUND("30001", "数据不存在"),
    DATA_ALREADY_EXISTS("30002", "数据已存在"),
    DATA_CONFLICT("30003", "数据冲突"),
    OPERATION_NOT_ALLOWED("30004", "操作不被允许"),
    STATUS_ERROR("30005", "状态错误"),
    
    // ========== 用户相关状态码 ==========
    USER_NOT_FOUND("31001", "用户不存在"),
    USER_ALREADY_EXISTS("31002", "用户已存在"),
    PASSWORD_ERROR("31003", "密码错误"),
    EMAIL_ALREADY_EXISTS("31004", "邮箱已存在"),
    PHONE_ALREADY_EXISTS("31005", "手机号已存在"),
    
    // ========== 文件相关状态码 ==========
    FILE_NOT_FOUND("32001", "文件不存在"),
    FILE_UPLOAD_FAILED("32002", "文件上传失败"),
    FILE_TYPE_NOT_SUPPORTED("32003", "文件类型不支持"),
    FILE_SIZE_EXCEEDED("32004", "文件大小超限"),
    FILE_DOWNLOAD_FAILED("32005", "文件下载失败"),
    
    // ========== 博客相关状态码 ==========
    ARTICLE_NOT_FOUND("33001", "文章不存在"),
    ARTICLE_ALREADY_PUBLISHED("33002", "文章已发布"),
    CATEGORY_NOT_FOUND("33003", "分类不存在"),
    TAG_NOT_FOUND("33004", "标签不存在"),
    COMMENT_NOT_FOUND("33005", "评论不存在"),
    
    // ========== 数据库相关状态码 ==========
    DATABASE_ERROR("40000", "数据库异常"),
    SQL_SYNTAX_ERROR("40001", "SQL语法错误"),
    DUPLICATE_KEY_ERROR("40002", "主键冲突"),
    FOREIGN_KEY_ERROR("40003", "外键约束错误"),
    CONNECTION_TIMEOUT("40004", "数据库连接超时"),
    
    // ========== 第三方服务状态码 ==========
    THIRD_PARTY_ERROR("50000", "第三方服务异常"),
    EMAIL_SEND_FAILED("50001", "邮件发送失败"),
    SMS_SEND_FAILED("50002", "短信发送失败"),
    OSS_UPLOAD_FAILED("50003", "对象存储上传失败"),
    PAYMENT_FAILED("50004", "支付失败"),
    
    // ========== 网络相关状态码 ==========
    NETWORK_ERROR("60000", "网络异常"),
    HTTP_CLIENT_ERROR("60001", "HTTP客户端错误"),
    HTTP_SERVER_ERROR("60002", "HTTP服务端错误"),
    CONNECT_TIMEOUT("60003", "连接超时"),
    READ_TIMEOUT("60004", "读取超时");
    
    /**
     * 状态码
     */
    private final String code;
    
    /**
     * 状态信息
     */
    private final String message;
    
    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return 状态码枚举
     */
    public static ResCodeEnum getByCode(String code) {
        for (ResCodeEnum errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return SYSTEM_ERROR;
    }
}