package ink.charter.website.common.job.core;

import com.fasterxml.jackson.core.type.TypeReference;
import ink.charter.website.common.core.utils.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 任务执行上下文
 * 封装任务执行过程中的相关信息和操作
 *
 * @author charter
 * @create 2025/07/30
 */
@Data
@Slf4j
public class JobExecuteContext {
    
    /**
     * 任务ID
     */
    private Long jobId;
    
    /**
     * 任务名称
     */
    private String jobName;
    
    /**
     * 任务分组
     */
    private String jobGroup;
    
    /**
     * 任务参数
     */
    private Map<String, Object> params;
    
    /**
     * 执行开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * Quartz执行上下文
     */
    private JobExecutionContext quartzContext;
    
    /**
     * 执行进度（0-100）
     */
    private int progress = 0;
    
    /**
     * 执行状态信息
     */
    private String statusMessage = "";
    
    public JobExecuteContext(JobExecutionContext quartzContext) {
        this.quartzContext = quartzContext;
        this.startTime = LocalDateTime.now();
        
        // 从Quartz上下文中获取任务信息
        this.jobId = quartzContext.getJobDetail().getJobDataMap().getLong("jobId");
        this.jobName = quartzContext.getJobDetail().getKey().getName();
        this.jobGroup = quartzContext.getJobDetail().getKey().getGroup();
        
        // 解析任务参数
        String paramsJson = quartzContext.getJobDetail().getJobDataMap().getString("params");
        if (paramsJson != null && !paramsJson.trim().isEmpty()) {
            try {
                this.params = JsonUtils.parseObject(paramsJson, new TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                log.warn("解析任务参数失败: {}", paramsJson, e);
                this.params = Map.of();
            }
        } else {
            this.params = Map.of();
        }
    }
    
    /**
     * 获取任务参数
     *
     * @param key 参数键
     * @return 参数值
     */
    public String getParam(String key) {
        Object value = params.get(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * 获取任务参数
     *
     * @param key 参数键
     * @param defaultValue 默认值
     * @return 参数值
     */
    public String getParam(String key, String defaultValue) {
        String value = getParam(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 获取整型参数
     *
     * @param key 参数键
     * @param defaultValue 默认值
     * @return 参数值
     */
    public Integer getIntParam(String key, Integer defaultValue) {
        try {
            String value = getParam(key);
            return value != null ? Integer.valueOf(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * 获取布尔型参数
     *
     * @param key 参数键
     * @param defaultValue 默认值
     * @return 参数值
     */
    public Boolean getBooleanParam(String key, Boolean defaultValue) {
        String value = getParam(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.valueOf(value);
    }
    
    /**
     * 更新执行进度
     *
     * @param progress 进度（0-100）
     * @param message 状态信息
     */
    public void updateProgress(int progress, String message) {
        this.progress = Math.max(0, Math.min(100, progress));
        this.statusMessage = message != null ? message : "";
        
        log.info("任务[{}]执行进度: {}%, 状态: {}", jobName, this.progress, this.statusMessage);
    }
    
    /**
     * 记录日志
     *
     * @param message 日志信息
     */
    public void log(String message) {
        log.info("任务[{}]: {}", jobName, message);
    }
    
    /**
     * 记录错误日志
     *
     * @param message 错误信息
     * @param throwable 异常
     */
    public void logError(String message, Throwable throwable) {
        log.error("任务[{}]错误: {}", jobName, message, throwable);
    }
}