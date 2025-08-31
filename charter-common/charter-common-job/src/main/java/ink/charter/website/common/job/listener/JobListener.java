package ink.charter.website.common.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

/**
 * 任务监听器
 * 监听任务的执行状态和生命周期事件
 *
 * @author charter
 * @create 2025/07/30
 */
@Slf4j
public class JobListener implements org.quartz.JobListener {
    
    private static final String LISTENER_NAME = "CharterJobListener";
    private static final String START_TIME_KEY = "startTime";
    
    @Override
    public String getName() {
        return LISTENER_NAME;
    }
    
    /**
     * 任务即将执行时调用
     *
     * @param context 任务执行上下文
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        log.debug("任务即将执行: [{}:{}]", jobKey.getGroup(), jobKey.getName());
        
        // 记录任务开始执行时间
        context.put(START_TIME_KEY, System.currentTimeMillis());
    }
    
    /**
     * 任务执行被否决时调用（通常是因为TriggerListener否决了执行）
     *
     * @param context 任务执行上下文
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        log.warn("任务执行被否决: [{}:{}]", jobKey.getGroup(), jobKey.getName());
    }
    
    /**
     * 任务执行完成后调用
     *
     * @param context 任务执行上下文
     * @param jobException 任务执行异常（如果有）
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobKey jobKey = context.getJobDetail().getKey();
        
        // 计算执行耗时
        Long startTime = (Long) context.get(START_TIME_KEY);
        long costTime = startTime != null ? System.currentTimeMillis() - startTime : 0;
        
        if (jobException != null) {
            log.error("任务执行失败: [{}:{}], 耗时: {}ms, 错误: {}", 
                    jobKey.getGroup(), jobKey.getName(), costTime, jobException.getMessage());
        } else {
            log.debug("任务执行完成: [{}:{}], 耗时: {}ms", 
                    jobKey.getGroup(), jobKey.getName(), costTime);
        }
        
        // JobExecutionContext在任务完成后会自动销毁，无需手动清理
    }
}