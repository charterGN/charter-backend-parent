package ink.charter.website.common.web.exception;

import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.exception.*;
import ink.charter.website.common.core.common.enums.ResCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理系统中的各种异常
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义基础异常
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("业务异常: {} - {} - {}", e.getErrorCode(), e.getErrorMessage(), request.getRequestURI(), e);
        Result<Void> result = Result.error(e.getErrorCode(), e.getErrorMessage());
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: {} - {} - {}", e.getErrorCode(), e.getErrorMessage(), request.getRequestURI());
        Result<Void> result = Result.error(e.getErrorCode(), e.getErrorMessage());
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("系统异常: {} - {} - {}", e.getErrorCode(), e.getErrorMessage(), request.getRequestURI(), e);
        Result<Void> result = Result.error(e.getErrorCode(), e.getErrorMessage());
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理认证授权异常
     */
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleAuthException(AuthException e, HttpServletRequest request) {
        log.warn("认证异常: {} - {} - {}", e.getErrorCode(), e.getErrorMessage(), request.getRequestURI());
        Result<Void> result = Result.error(e.getErrorCode(), e.getErrorMessage());
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(ValidationException e, HttpServletRequest request) {
        log.warn("参数验证异常: {} - {} - {}", e.getErrorCode(), e.getErrorMessage(), request.getRequestURI());
        Result<Void> result = Result.error(e.getErrorCode(), e.getErrorMessage());
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理参数绑定异常（@RequestBody 参数校验失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败: {} - {}", errorMessage, request.getRequestURI());
        Result<Void> result = Result.error(ResCodeEnum.PARAM_INVALID.getCode(), errorMessage);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理参数绑定异常（@ModelAttribute 参数校验失败）
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数绑定异常: {} - {}", errorMessage, request.getRequestURI());
        Result<Void> result = Result.error(ResCodeEnum.PARAM_INVALID.getCode(), errorMessage);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理约束违反异常（@RequestParam 参数校验失败）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("约束违反异常: {} - {}", errorMessage, request.getRequestURI());
        Result<Void> result = Result.error(ResCodeEnum.PARAM_INVALID.getCode(), errorMessage);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String errorMessage = "缺少必需的请求参数: " + e.getParameterName();
        log.warn("缺少请求参数: {} - {}", errorMessage, request.getRequestURI());
        Result<Void> result = Result.error(ResCodeEnum.PARAM_MISSING.getCode(), errorMessage);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String errorMessage = "参数类型不匹配: " + e.getName() + "，期望类型: " + e.getRequiredType().getSimpleName();
        log.warn("参数类型不匹配: {} - {}", errorMessage, request.getRequestURI());
        Result<Void> result = Result.error(ResCodeEnum.PARAM_INVALID.getCode(), errorMessage);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理HTTP消息不可读异常（JSON格式错误等）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        String errorMessage = "请求体格式错误，请检查JSON格式";
        log.warn("HTTP消息不可读: {} - {}", e.getMessage(), request.getRequestURI());
        Result<Void> result = Result.error(ResCodeEnum.PARAM_INVALID.getCode(), errorMessage);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String errorMessage = "不支持的请求方法: " + e.getMethod() + "，支持的方法: " + String.join(", ", e.getSupportedMethods());
        log.warn("请求方法不支持: {} - {}", errorMessage, request.getRequestURI());
        Result<Void> result = Result.error(ResCodeEnum.REQUEST_METHOD_NOT_SUPPORTED.getCode(), errorMessage);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String errorMessage = "请求的资源不存在: " + e.getRequestURL();
        log.warn("404异常: {} - {}", errorMessage, request.getRequestURI());
        Result<Void> result = Result.error("40400", errorMessage);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        String errorMessage = "上传文件大小超出限制: " + e.getMaxUploadSize() + " bytes";
        log.warn("文件上传大小超限: {} - {}", errorMessage, request.getRequestURI());
        Result<Void> result = Result.error(ResCodeEnum.FILE_SIZE_EXCEEDED.getCode(), errorMessage);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理访问拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("访问拒绝: {} - {}", e.getMessage(), request.getRequestURI());
        Result<Void> result = Result.error(ResCodeEnum.PERMISSION_DENIED);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("数据库异常: {} - {}", e.getMessage(), request.getRequestURI(), e);
        Result<Void> result = Result.error(ResCodeEnum.DATABASE_ERROR);
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常: {} - {}", e.getMessage(), request.getRequestURI(), e);
        Result<Void> result = Result.error(ResCodeEnum.SYSTEM_ERROR.getCode(), "系统内部错误");
        return result.requestId(getRequestId(request));
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("未知异常: {} - {}", e.getMessage(), request.getRequestURI(), e);
        Result<Void> result = Result.error(ResCodeEnum.SYSTEM_ERROR);
        return result.requestId(getRequestId(request));
    }

    /**
     * 获取请求ID（用于链路追踪）
     */
    private String getRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = request.getHeader("X-Trace-ID");
        }
        return requestId;
    }
}