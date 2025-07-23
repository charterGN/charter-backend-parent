package ink.charter.website.common.core.exception;

import ink.charter.website.common.core.common.enums.ResCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础异常类
 * 所有自定义异常的父类
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 业务模块
     */
    private String module;

    /**
     * 详细错误信息（用于日志记录）
     */
    private String detailMessage;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public BaseException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BaseException(ResCodeEnum resCodeEnum) {
        super(resCodeEnum.getMessage());
        this.errorCode = resCodeEnum.getCode();
        this.errorMessage = resCodeEnum.getMessage();
    }

    public BaseException(ResCodeEnum resCodeEnum, String module) {
        super(resCodeEnum.getMessage());
        this.errorCode = resCodeEnum.getCode();
        this.errorMessage = resCodeEnum.getMessage();
        this.module = module;
    }

    public BaseException(String errorCode, String errorMessage, String module) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.module = module;
    }

    public BaseException(String errorCode, String errorMessage, String module, String detailMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.module = module;
        this.detailMessage = detailMessage;
    }

    public BaseException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BaseException(ResCodeEnum resCodeEnum, Throwable cause) {
        super(resCodeEnum.getMessage(), cause);
        this.errorCode = resCodeEnum.getCode();
        this.errorMessage = resCodeEnum.getMessage();
    }
}