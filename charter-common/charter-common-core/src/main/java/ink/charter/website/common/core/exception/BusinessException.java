package ink.charter.website.common.core.exception;

import ink.charter.website.common.core.common.enums.ResCodeEnum;

/**
 * 业务异常类
 * 用于处理业务逻辑中的异常情况
 *
 * @author charter
 * @create 2025/07/17
 */
public class BusinessException extends BaseException {

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public BusinessException(ResCodeEnum resCodeEnum) {
        super(resCodeEnum);
    }

    public BusinessException(ResCodeEnum resCodeEnum, String module) {
        super(resCodeEnum, module);
    }

    public BusinessException(String errorCode, String errorMessage, String module) {
        super(errorCode, errorMessage, module);
    }

    public BusinessException(String errorCode, String errorMessage, String module, String detailMessage) {
        super(errorCode, errorMessage, module, detailMessage);
    }

    public BusinessException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public BusinessException(ResCodeEnum resCodeEnum, Throwable cause) {
        super(resCodeEnum, cause);
    }

    /**
     * 快速创建业务异常
     *
     * @param message 错误信息
     * @return 业务异常
     */
    public static BusinessException of(String message) {
        return new BusinessException(ResCodeEnum.BUSINESS_ERROR.getCode(), message);
    }

    /**
     * 快速创建业务异常
     *
     * @param resCodeEnum 错误码枚举
     * @return 业务异常
     */
    public static BusinessException of(ResCodeEnum resCodeEnum) {
        return new BusinessException(resCodeEnum);
    }

    /**
     * 快速创建业务异常
     *
     * @param resCodeEnum 错误码枚举
     * @param module 业务模块
     * @return 业务异常
     */
    public static BusinessException of(ResCodeEnum resCodeEnum, String module) {
        return new BusinessException(resCodeEnum, module);
    }

    /**
     * 数据不存在异常
     *
     * @return 业务异常
     */
    public static BusinessException dataNotFound() {
        return new BusinessException(ResCodeEnum.DATA_NOT_FOUND);
    }

    /**
     * 数据不存在异常
     *
     * @param dataType 数据类型
     * @return 业务异常
     */
    public static BusinessException dataNotFound(String dataType) {
        return new BusinessException(ResCodeEnum.DATA_NOT_FOUND.getCode(),
                dataType + "不存在");
    }

    /**
     * 数据已存在异常
     *
     * @return 业务异常
     */
    public static BusinessException dataAlreadyExists() {
        return new BusinessException(ResCodeEnum.DATA_ALREADY_EXISTS);
    }

    /**
     * 数据已存在异常
     *
     * @param dataType 数据类型
     * @return 业务异常
     */
    public static BusinessException dataAlreadyExists(String dataType) {
        return new BusinessException(ResCodeEnum.DATA_ALREADY_EXISTS.getCode(),
                dataType + "已存在");
    }

    /**
     * 操作不被允许异常
     *
     * @return 业务异常
     */
    public static BusinessException operationNotAllowed() {
        return new BusinessException(ResCodeEnum.OPERATION_NOT_ALLOWED);
    }

    /**
     * 操作不被允许异常
     *
     * @param operation 操作名称
     * @return 业务异常
     */
    public static BusinessException operationNotAllowed(String operation) {
        return new BusinessException(ResCodeEnum.OPERATION_NOT_ALLOWED.getCode(),
                operation + "操作不被允许");
    }
}