package ink.charter.website.common.core.exception;

import ink.charter.website.common.core.common.enums.ResCodeEnum;

/**
 * 参数验证异常类
 * 用于处理请求参数验证相关的异常
 *
 * @author charter
 * @create 2025/07/17
 */
public class ValidationException extends BaseException {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public ValidationException(ResCodeEnum resCodeEnum) {
        super(resCodeEnum);
    }

    public ValidationException(ResCodeEnum resCodeEnum, String module) {
        super(resCodeEnum, module);
    }

    public ValidationException(String errorCode, String errorMessage, String module) {
        super(errorCode, errorMessage, module);
    }

    public ValidationException(String errorCode, String errorMessage, String module, String detailMessage) {
        super(errorCode, errorMessage, module, detailMessage);
    }

    public ValidationException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public ValidationException(ResCodeEnum resCodeEnum, Throwable cause) {
        super(resCodeEnum, cause);
    }

    /**
     * 快速创建参数验证异常
     *
     * @param message 错误信息
     * @return 参数验证异常
     */
    public static ValidationException of(String message) {
        return new ValidationException(ResCodeEnum.PARAM_ERROR.getCode(), message);
    }

    /**
     * 快速创建参数验证异常
     *
     * @param resCodeEnum 错误码枚举
     * @return 参数验证异常
     */
    public static ValidationException of(ResCodeEnum resCodeEnum) {
        return new ValidationException(resCodeEnum);
    }

    /**
     * 参数错误异常
     *
     * @return 参数验证异常
     */
    public static ValidationException paramError() {
        return new ValidationException(ResCodeEnum.PARAM_ERROR);
    }

    /**
     * 参数错误异常
     *
     * @param paramName 参数名称
     * @return 参数验证异常
     */
    public static ValidationException paramError(String paramName) {
        return new ValidationException(ResCodeEnum.PARAM_ERROR.getCode(),
                "参数[" + paramName + "]错误");
    }

    /**
     * 参数缺失异常
     *
     * @return 参数验证异常
     */
    public static ValidationException paramMissing() {
        return new ValidationException(ResCodeEnum.PARAM_MISSING);
    }

    /**
     * 参数缺失异常
     *
     * @param paramName 参数名称
     * @return 参数验证异常
     */
    public static ValidationException paramMissing(String paramName) {
        return new ValidationException(ResCodeEnum.PARAM_MISSING.getCode(),
                "参数[" + paramName + "]缺失");
    }

    /**
     * 参数格式错误异常
     *
     * @return 参数验证异常
     */
    public static ValidationException paramInvalid() {
        return new ValidationException(ResCodeEnum.PARAM_INVALID);
    }

    /**
     * 参数格式错误异常
     *
     * @param paramName 参数名称
     * @return 参数验证异常
     */
    public static ValidationException paramInvalid(String paramName) {
        return new ValidationException(ResCodeEnum.PARAM_INVALID.getCode(),
                "参数[" + paramName + "]格式错误");
    }

    /**
     * 参数格式错误异常
     *
     * @param paramName 参数名称
     * @param expectedFormat 期望格式
     * @return 参数验证异常
     */
    public static ValidationException paramInvalid(String paramName, String expectedFormat) {
        return new ValidationException(ResCodeEnum.PARAM_INVALID.getCode(),
                "参数[" + paramName + "]格式错误，期望格式：" + expectedFormat);
    }

    /**
     * 参数值超出范围异常
     *
     * @param paramName 参数名称
     * @param minValue 最小值
     * @param maxValue 最大值
     * @return 参数验证异常
     */
    public static ValidationException paramOutOfRange(String paramName, Object minValue, Object maxValue) {
        return new ValidationException(ResCodeEnum.PARAM_INVALID.getCode(),
                "参数[" + paramName + "]超出范围，有效范围：[" + minValue + ", " + maxValue + "]");
    }

    /**
     * 参数长度错误异常
     *
     * @param paramName 参数名称
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 参数验证异常
     */
    public static ValidationException paramLengthError(String paramName, int minLength, int maxLength) {
        return new ValidationException(ResCodeEnum.PARAM_INVALID.getCode(),
                "参数[" + paramName + "]长度错误，有效长度：[" + minLength + ", " + maxLength + "]");
    }
}