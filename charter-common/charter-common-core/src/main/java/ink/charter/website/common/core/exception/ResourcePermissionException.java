package ink.charter.website.common.core.exception;

import ink.charter.website.common.core.common.enums.ResCodeEnum;
import lombok.Getter;

/**
 * 资源权限异常
 * 当用户没有访问特定资源的权限时抛出
 *
 * @author charter
 * @create 2025/09/30
 */
@Getter
public class ResourcePermissionException extends BaseException {

    public ResourcePermissionException() {
        super();
    }

    public ResourcePermissionException(String message) {
        super(message);
    }

    public ResourcePermissionException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public ResourcePermissionException(ResCodeEnum resCodeEnum) {
        super(resCodeEnum);
    }

    public ResourcePermissionException(ResCodeEnum resCodeEnum, String module) {
        super(resCodeEnum, module);
    }

    public ResourcePermissionException(String errorCode, String errorMessage, String module) {
        super(errorCode, errorMessage, module);
    }

    public ResourcePermissionException(String errorCode, String errorMessage, String module, String detailMessage) {
        super(errorCode, errorMessage, module, detailMessage);
    }

    public ResourcePermissionException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public ResourcePermissionException(ResCodeEnum resCodeEnum, Throwable cause) {
        super(resCodeEnum, cause);
    }

    /**
     * 快速创建资源权限异常
     *
     * @param message 错误信息
     * @return 资源权限异常
     */
    public static ResourcePermissionException of(String message) {
        return new ResourcePermissionException(ResCodeEnum.PERMISSION_DENIED.getCode(), message);
    }

    /**
     * 快速创建资源权限异常
     *
     * @param resCodeEnum 错误码枚举
     * @return 资源权限异常
     */
    public static ResourcePermissionException of(ResCodeEnum resCodeEnum) {
        return new ResourcePermissionException(resCodeEnum);
    }

    /**
     * 资源权限不足异常
     *
     * @return 资源权限异常
     */
    public static ResourcePermissionException permissionDenied() {
        return new ResourcePermissionException(ResCodeEnum.PERMISSION_DENIED);
    }

    /**
     * 资源权限不足异常
     *
     * @param message 错误信息
     * @return 资源权限异常
     */
    public static ResourcePermissionException permissionDenied(String message) {
        return new ResourcePermissionException(ResCodeEnum.PERMISSION_DENIED.getCode(), message);
    }

    /**
     * 资源权限不足异常
     *
     * @param resourceCode 资源代码
     * @return 资源权限异常
     */
    public static ResourcePermissionException resourceDenied(String resourceCode) {
        return new ResourcePermissionException(ResCodeEnum.PERMISSION_DENIED.getCode(),
                "无权限访问资源：" + resourceCode);
    }

    /**
     * 资源不存在异常
     *
     * @param resourceCode 资源代码
     * @return 资源权限异常
     */
    public static ResourcePermissionException resourceNotFound(String resourceCode) {
        return new ResourcePermissionException(ResCodeEnum.DATA_NOT_FOUND.getCode(),
                "资源不存在：" + resourceCode);
    }
}