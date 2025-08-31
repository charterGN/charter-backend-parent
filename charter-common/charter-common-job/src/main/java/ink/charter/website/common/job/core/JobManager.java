package ink.charter.website.common.job.core;

import ink.charter.website.common.job.utils.CronUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;
import java.util.List;

/**
 * 任务管理器
 * 负责任务的创建、启动、暂停、删除等操作
 *
 * @author charter
 * @create 2025/07/30
 */
@Slf4j
@RequiredArgsConstructor
public class JobManager {
    
    private final Scheduler scheduler;
    
    /**
     * 创建并启动任务
     *
     * @param jobId 任务ID
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @param handlerName 处理器名称
     * @param cronExpression Cron表达式
     * @param params 任务参数
     * @param concurrent 是否允许并发
     * @throws SchedulerException 调度异常
     */
    public void createJob(Long jobId, String jobName, String jobGroup, String handlerName, 
                         String cronExpression, String params, Boolean concurrent) throws SchedulerException {
        
        // 验证Cron表达式
        if (!CronUtils.isValid(cronExpression)) {
            throw new IllegalArgumentException("无效的Cron表达式: " + cronExpression);
        }
        
        // 创建JobKey
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        
        // 如果任务已存在，先删除
        if (scheduler.checkExists(jobKey)) {
            log.warn("任务[{}:{}]已存在，将先删除后重新创建", jobGroup, jobName);
            scheduler.deleteJob(jobKey);
        }
        
        // 创建JobDetail
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobId", jobId);
        jobDataMap.put("handlerName", handlerName);
        jobDataMap.put("params", params);
        jobDataMap.put("concurrent", concurrent);
        
        JobDetail jobDetail = JobBuilder.newJob(JobExecutor.class)
                .withIdentity(jobKey)
                .withDescription("定时任务: " + jobName)
                .setJobData(jobDataMap)
                .storeDurably(false)
                .build();
        
        // 创建Trigger
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                        .withMisfireHandlingInstructionDoNothing()) // 错过执行时不执行
                .build();
        
        // 调度任务
        scheduler.scheduleJob(jobDetail, trigger);
        
        log.info("创建任务成功: [{}:{}], Cron: {}, 处理器: {}", jobGroup, jobName, cronExpression, handlerName);
    }
    
    /**
     * 启动任务
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 调度异常
     */
    public void startJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        
        if (!scheduler.checkExists(jobKey)) {
            throw new IllegalArgumentException("任务不存在: " + jobGroup + ":" + jobName);
        }
        
        scheduler.resumeJob(jobKey);
        log.info("启动任务: [{}:{}]", jobGroup, jobName);
    }
    
    /**
     * 暂停任务
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 调度异常
     */
    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        
        if (!scheduler.checkExists(jobKey)) {
            throw new IllegalArgumentException("任务不存在: " + jobGroup + ":" + jobName);
        }
        
        scheduler.pauseJob(jobKey);
        log.info("暂停任务: [{}:{}]", jobGroup, jobName);
    }
    
    /**
     * 删除任务
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 调度异常
     */
    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        
        if (!scheduler.checkExists(jobKey)) {
            throw new IllegalArgumentException("任务不存在: " + jobGroup + ":" + jobName);
        }
        
        scheduler.deleteJob(jobKey);
        log.info("删除任务: [{}:{}]", jobGroup, jobName);
    }
    
    /**
     * 立即执行任务
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 调度异常
     */
    public void executeJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        
        if (!scheduler.checkExists(jobKey)) {
            throw new IllegalArgumentException("任务不存在: " + jobGroup + ":" + jobName);
        }
        
        scheduler.triggerJob(jobKey);
        log.info("立即执行任务: [{}:{}]", jobGroup, jobName);
    }
    
    /**
     * 更新任务的Cron表达式
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @param cronExpression 新的Cron表达式
     * @throws SchedulerException 调度异常
     */
    public void updateJobCron(String jobName, String jobGroup, String cronExpression) throws SchedulerException {
        // 验证Cron表达式
        if (!CronUtils.isValid(cronExpression)) {
            throw new IllegalArgumentException("无效的Cron表达式: " + cronExpression);
        }
        
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        
        if (!scheduler.checkExists(triggerKey)) {
            throw new IllegalArgumentException("任务触发器不存在: " + jobGroup + ":" + jobName);
        }
        
        // 创建新的Trigger
        Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                        .withMisfireHandlingInstructionDoNothing())
                .build();
        
        // 重新调度
        scheduler.rescheduleJob(triggerKey, newTrigger);
        
        log.info("更新任务Cron表达式: [{}:{}], 新Cron: {}", jobGroup, jobName, cronExpression);
    }
    
    /**
     * 获取任务状态
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @return 任务状态
     * @throws SchedulerException 调度异常
     */
    public Trigger.TriggerState getJobState(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.getTriggerState(triggerKey);
    }
    
    /**
     * 获取任务下次执行时间
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @return 下次执行时间
     * @throws SchedulerException 调度异常
     */
    public Date getJobNextFireTime(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        return trigger != null ? trigger.getNextFireTime() : null;
    }
    
    /**
     * 获取正在执行的任务
     *
     * @return 正在执行的任务列表
     * @throws SchedulerException 调度异常
     */
    public List<JobExecutionContext> getRunningJobs() throws SchedulerException {
        return scheduler.getCurrentlyExecutingJobs();
    }
    
    /**
     * 检查任务是否存在
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @return true-存在，false-不存在
     * @throws SchedulerException 调度异常
     */
    public boolean jobExists(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        return scheduler.checkExists(jobKey);
    }
}