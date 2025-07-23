package ink.charter.website.common.core.exception;

import ink.charter.website.common.core.common.enums.ResCodeEnum;

/**
 * 系统异常类
 * 用于处理系统级别的异常情况
 *
 * @author charter
 * @create 2025/07/17
 */
public class SystemException extends BaseException {

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public SystemException(ResCodeEnum resCodeEnum) {
        super(resCodeEnum);
    }

    public SystemException(ResCodeEnum resCodeEnum, String module) {
        super(resCodeEnum, module);
    }

    public SystemException(String errorCode, String errorMessage, String module) {
        super(errorCode, errorMessage, module);
    }

    public SystemException(String errorCode, String errorMessage, String module, String detailMessage) {
        super(errorCode, errorMessage, module, detailMessage);
    }

    public SystemException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public SystemException(ResCodeEnum resCodeEnum, Throwable cause) {
        super(resCodeEnum, cause);
    }

    /**
     * 快速创建系统异常
     *
     * @param message 错误信息
     * @return 系统异常
     */
    public static SystemException of(String message) {
        return new SystemException(ResCodeEnum.SYSTEM_ERROR.getCode(), message);
    }

    /**
     * 快速创建系统异常
     *
     * @param resCodeEnum 错误码枚举
     * @return 系统异常
     */
    public static SystemException of(ResCodeEnum resCodeEnum) {
        return new SystemException(resCodeEnum);
    }

    /**
     * 快速创建系统异常
     *
     * @param resCodeEnum 错误码枚举
     * @param cause 原始异常
     * @return 系统异常
     */
    public static SystemException of(ResCodeEnum resCodeEnum, Throwable cause) {
        return new SystemException(resCodeEnum, cause);
    }

    /**
     * 数据库异常
     *
     * @return 系统异常
     */
    public static SystemException databaseError() {
        return new SystemException(ResCodeEnum.DATABASE_ERROR);
    }

    /**
     * 数据库异常
     *
     * @param cause 原始异常
     * @return 系统异常
     */
    public static SystemException databaseError(Throwable cause) {
        return new SystemException(ResCodeEnum.DATABASE_ERROR, cause);
    }

    /**
     * 网络异常
     *
     * @return 系统异常
     */
    public static SystemException networkError() {
        return new SystemException(ResCodeEnum.NETWORK_ERROR);
    }

    /**
     * 网络异常
     *
     * @param cause 原始异常
     * @return 系统异常
     */
    public static SystemException networkError(Throwable cause) {
        return new SystemException(ResCodeEnum.NETWORK_ERROR, cause);
    }

    /**
     * 第三方服务异常
     *
     * @return 系统异常
     */
    public static SystemException thirdPartyError() {
        return new SystemException(ResCodeEnum.THIRD_PARTY_ERROR);
    }

    /**
     * 第三方服务异常
     *
     * @param serviceName 服务名称
     * @return 系统异常
     */
    public static SystemException thirdPartyError(String serviceName) {
        return new SystemException(ResCodeEnum.THIRD_PARTY_ERROR.getCode(),
                serviceName + "服务异常");
    }

    /**
     * 第三方服务异常
     *
     * @param serviceName 服务名称
     * @param cause 原始异常
     * @return 系统异常
     */
    public static SystemException thirdPartyError(String serviceName, Throwable cause) {
        return new SystemException(ResCodeEnum.THIRD_PARTY_ERROR.getCode(),
                serviceName + "服务异常", cause);
    }
}