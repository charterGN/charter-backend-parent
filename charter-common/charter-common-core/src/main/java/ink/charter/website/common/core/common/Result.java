package ink.charter.website.common.core.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import ink.charter.website.common.core.common.enums.ResCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统一响应结果类
 * 用于封装API响应数据
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Result", description = "统一响应结果")
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 11404L;

    /**
     * 响应码
     */
    @Schema(description = "响应码")
    private String code;

    /**
     * 响应消息
     */
    @Schema(description = "响应消息")
    private String message;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    /**
     * 响应时间戳
     */
    @Schema(description = "响应时间戳")
    private LocalDateTime timestamp;

    /**
     * 请求ID（用于链路追踪）
     */
    @Schema(description = "请求ID")
    private String requestId;

    /**
     * 是否成功
     */
    @Schema(description = "是否成功")
    private Boolean success;

    public Result() {
        this.timestamp = LocalDateTime.now();
    }

    public Result(String code, String message) {
        this();
        this.code = code;
        this.message = message;
        this.success = ResCodeEnum.SUCCESS.getCode().equals(code);
    }

    public Result(String code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public Result(ResCodeEnum resCodeEnum) {
        this(resCodeEnum.getCode(), resCodeEnum.getMessage());
    }

    public Result(ResCodeEnum resCodeEnum, T data) {
        this(resCodeEnum.getCode(), resCodeEnum.getMessage(), data);
    }

    /**
     * 成功响应
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success() {
        return new Result<>(ResCodeEnum.SUCCESS);
    }

    /**
     * 成功响应
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResCodeEnum.SUCCESS, data);
    }

    /**
     * 成功响应
     *
     * @param message 响应消息
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(ResCodeEnum.SUCCESS.getCode(), message);
    }

    /**
     * 成功响应
     *
     * @param message 响应消息
     * @param data    响应数据
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败响应
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error() {
        return new Result<>(ResCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 失败响应
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ResCodeEnum.SYSTEM_ERROR.getCode(), message);
    }

    /**
     * 失败响应
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error(String code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 失败响应
     *
     * @param resCodeEnum 错误码枚举
     * @param <T>           数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error(ResCodeEnum resCodeEnum) {
        return new Result<>(resCodeEnum);
    }

    /**
     * 失败响应
     *
     * @param resCodeEnum 错误码枚举
     * @param data          响应数据
     * @param <T>           数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error(ResCodeEnum resCodeEnum, T data) {
        return new Result<>(resCodeEnum, data);
    }

    /**
     * 判断是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return ResCodeEnum.SUCCESS.getCode().equals(this.code);
    }

    /**
     * 判断是否失败
     *
     * @return 是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 设置请求ID
     *
     * @param requestId 请求ID
     * @return 响应结果
     */
    public Result<T> requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}