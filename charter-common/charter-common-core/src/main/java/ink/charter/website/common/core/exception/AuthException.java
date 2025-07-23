package ink.charter.website.common.core.exception;

import ink.charter.website.common.core.common.enums.ResCodeEnum;

/**
 * 认证授权异常类
 * 用于处理用户认证和权限相关的异常
 *
 * @author charter
 * @create 2025/07/17
 */
public class AuthException extends BaseException {

    public AuthException() {
        super();
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public AuthException(ResCodeEnum resCodeEnum) {
        super(resCodeEnum);
    }

    public AuthException(ResCodeEnum resCodeEnum, String module) {
        super(resCodeEnum, module);
    }

    public AuthException(String errorCode, String errorMessage, String module) {
        super(errorCode, errorMessage, module);
    }

    public AuthException(String errorCode, String errorMessage, String module, String detailMessage) {
        super(errorCode, errorMessage, module, detailMessage);
    }

    public AuthException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public AuthException(ResCodeEnum resCodeEnum, Throwable cause) {
        super(resCodeEnum, cause);
    }

    /**
     * 快速创建认证异常
     *
     * @param message 错误信息
     * @return 认证异常
     */
    public static AuthException of(String message) {
        return new AuthException(ResCodeEnum.UNAUTHORIZED.getCode(), message);
    }

    /**
     * 快速创建认证异常
     *
     * @param resCodeEnum 错误码枚举
     * @return 认证异常
     */
    public static AuthException of(ResCodeEnum resCodeEnum) {
        return new AuthException(resCodeEnum);
    }

    /**
     * 未认证异常
     *
     * @return 认证异常
     */
    public static AuthException unauthorized() {
        return new AuthException(ResCodeEnum.UNAUTHORIZED);
    }

    /**
     * 未认证异常
     *
     * @param message 错误信息
     * @return 认证异常
     */
    public static AuthException unauthorized(String message) {
        return new AuthException(ResCodeEnum.UNAUTHORIZED.getCode(), message);
    }

    /**
     * Token无效异常
     *
     * @return 认证异常
     */
    public static AuthException tokenInvalid() {
        return new AuthException(ResCodeEnum.TOKEN_INVALID);
    }

    /**
     * Token过期异常
     *
     * @return 认证异常
     */
    public static AuthException tokenExpired() {
        return new AuthException(ResCodeEnum.TOKEN_EXPIRED);
    }

    /**
     * 权限不足异常
     *
     * @return 认证异常
     */
    public static AuthException permissionDenied() {
        return new AuthException(ResCodeEnum.PERMISSION_DENIED);
    }

    /**
     * 权限不足异常
     *
     * @param resource 资源名称
     * @return 认证异常
     */
    public static AuthException permissionDenied(String resource) {
        return new AuthException(ResCodeEnum.PERMISSION_DENIED.getCode(),
                "无权限访问" + resource);
    }

    /**
     * 登录失败异常
     *
     * @return 认证异常
     */
    public static AuthException loginFailed() {
        return new AuthException(ResCodeEnum.LOGIN_FAILED);
    }

    /**
     * 登录失败异常
     *
     * @param reason 失败原因
     * @return 认证异常
     */
    public static AuthException loginFailed(String reason) {
        return new AuthException(ResCodeEnum.LOGIN_FAILED.getCode(),
                "登录失败：" + reason);
    }

    /**
     * 账户锁定异常
     *
     * @return 认证异常
     */
    public static AuthException accountLocked() {
        return new AuthException(ResCodeEnum.ACCOUNT_LOCKED);
    }

    /**
     * 账户禁用异常
     *
     * @return 认证异常
     */
    public static AuthException accountDisabled() {
        return new AuthException(ResCodeEnum.ACCOUNT_DISABLED);
    }

    /**
     * 密码错误异常
     *
     * @return 认证异常
     */
    public static AuthException passwordError() {
        return new AuthException(ResCodeEnum.PASSWORD_ERROR);
    }
}