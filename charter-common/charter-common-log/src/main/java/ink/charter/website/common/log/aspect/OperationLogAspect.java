package ink.charter.website.common.log.aspect;

import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.config.LogProperties;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.common.log.service.OperationLogService;
import ink.charter.website.common.log.utils.LogUtils;
import ink.charter.website.common.core.entity.sys.SysOptLogEntity;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志切面
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogService operationLogService;
    
    private final LogProperties logProperties;

    /**
     * 定义切点：所有标注了@OperationLog注解的方法
     */
    @Pointcut("@annotation(ink.charter.website.common.log.annotation.OperationLog)")
    public void operationLogPointcut() {
    }

    /**
     * 环绕通知：记录操作日志
     */
    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 判断是否启用日志
        if (!logProperties.isEnabled()) {
            // 如果日志功能未启用，直接执行目标方法
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();
        
        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog = method.getAnnotation(OperationLog.class);
        
        if (operationLog == null) {
            return joinPoint.proceed();
        }
        
        // 获取请求信息
        HttpServletRequest request = getHttpServletRequest();
        
        // 构建日志实体
        SysOptLogEntity logEntity = buildLogEntity(joinPoint, operationLog, request);
        
        Object result = null;
        Throwable exception = null;
        
        try {
            // 执行目标方法
            result = joinPoint.proceed();
            
            // 设置操作状态为成功
            logEntity.setOptStatus(LogConstant.OptStatus.SUCCESS);
            
            // 记录响应数据
            if (operationLog.recordResponse() && result != null) {
                String responseJson = LogUtils.toJsonString(result);
                logEntity.setResponseData(LogUtils.truncateString(responseJson, logProperties.getMaxResponseLength()));
            }
            
        } catch (Throwable e) {
            exception = e;
            
            // 设置操作状态为失败
            logEntity.setOptStatus(LogConstant.OptStatus.FAIL);
            
            // 记录错误信息
            String errorMsg = e.getMessage();
            if (errorMsg == null) {
                errorMsg = e.getClass().getSimpleName();
            }
            logEntity.setErrorMsg(LogUtils.truncateString(errorMsg, logProperties.getMaxErrorLength()));
            
            log.error("业务方法执行异常: {}", errorMsg, e);
        } finally {
            // 计算执行时间
            long costTime = System.currentTimeMillis() - startTime;
            logEntity.setOptCostTime(costTime);
            
            // 保存日志
            try {
                if (operationLog.async()) {
                    operationLogService.saveLogAsync(logEntity);
                } else {
                    operationLogService.saveLog(logEntity);
                }
            } catch (Exception logException) {
                log.error("操作日志记录异常", logException);
                // 即使日志记录失败，也不影响业务方法的执行
            }
        }
        
        // 如果有异常，重新抛出
        if (exception != null) {
            throw exception;
        }
        
        return result;
    }

    /**
     * 构建日志实体
     */
    private SysOptLogEntity buildLogEntity(JoinPoint joinPoint, OperationLog operationLog, 
                                          HttpServletRequest request) {
        SysOptLogEntity logEntity = new SysOptLogEntity();
        
        // 设置操作时间
        logEntity.setCreateTime(LocalDateTime.now());
        
        // 设置操作信息
        logEntity.setOptModule(operationLog.module());
        logEntity.setOptType(operationLog.type());
        logEntity.setOptDesc(operationLog.description());
        
        // 设置方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        logEntity.setOptMethod(LogUtils.getFullMethodName(className, methodName));
        
        // 设置请求信息
        if (request != null) {
            logEntity.setOptUrl(LogUtils.getRequestUri(request));
            logEntity.setRequestMethod(LogUtils.getRequestMethod(request));
            
            // 获取客户端IP
            String clientIp = LogUtils.getClientIp(request);
            logEntity.setOptIp(clientIp);
            
            // 获取IP地址归属地
            if (logProperties.isEnableIpResolve()) {
                try {
                    String address = LogUtils.getAddressByIp(clientIp);
                    logEntity.setOptAddress(address);
                } catch (Exception e) {
                    log.warn("获取IP地址归属地失败: {}", e.getMessage());
                    logEntity.setOptAddress(LogConstant.IpAddress.UNKNOWN_REGION);
                }
            } else {
                logEntity.setOptAddress(LogUtils.isLocalIp(clientIp) ? 
                    LogConstant.IpAddress.INTRANET : LogConstant.IpAddress.UNKNOWN_REGION);
            }
            
            // 设置网站信息
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            if (serverPort != 80 && serverPort != 443) {
                logEntity.setOptWebsite(serverName + ":" + serverPort);
            } else {
                logEntity.setOptWebsite(serverName);
            }
        }
        
        // 设置请求参数
        if (operationLog.recordParams()) {
            Map<String, Object> params = getMethodParams(joinPoint);
            Map<String, Object> filteredParams = LogUtils.filterSensitiveParams(params, operationLog.ignoreParams());
            String paramJson = LogUtils.toJsonString(filteredParams);
            logEntity.setRequestParam(LogUtils.truncateString(paramJson, logProperties.getMaxParamLength()));
        }
        
        // 设置操作用户信息
        try {
            Long userId = LogUtils.getCurrentUserId();
            String username = LogUtils.getCurrentUsername();
            
            if (userId != null) {
                logEntity.setOptId(userId);
            }
            if (username != null) {
                logEntity.setOptName(username);
            }
        } catch (Exception e) {
            log.warn("获取当前用户信息失败: {}", e.getMessage());
        }
        
        return logEntity;
    }

    /**
     * 获取方法参数
     */
    private Map<String, Object> getMethodParams(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        
        if (paramNames != null && paramValues != null) {
            for (int i = 0; i < paramNames.length && i < paramValues.length; i++) {
                Object paramValue = paramValues[i];

                // 过滤掉Servlet API相关的参数，因为其不可序列化
                if (paramValue instanceof ServletRequest || paramValue instanceof ServletResponse) {
                    continue;
                }
                
                params.put(paramNames[i], paramValue);
            }
        }
        
        return params;
    }

    /**
     * 获取HttpServletRequest
     */
    private HttpServletRequest getHttpServletRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes.getRequest();
        } catch (Exception e) {
            log.warn("获取HttpServletRequest失败", e);
            return null;
        }
    }


}