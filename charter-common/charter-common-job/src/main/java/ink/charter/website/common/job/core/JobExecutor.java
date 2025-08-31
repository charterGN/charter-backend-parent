package ink.charter.website.common.job.core;

import ink.charter.website.common.job.annotation.DisallowConcurrent;
import ink.charter.website.common.job.handler.AbstractJobHandler;
import ink.charter.website.common.job.handler.JobHandlerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 任务执行器
 * 实现Quartz的Job接口，负责调用具体的任务处理器
 *
 * @author charter
 * @create 2025/07/30
 */
@Slf4j
@RequiredArgsConstructor
@DisallowConcurrentExecution // 默认禁止并发执行，具体由任务配置决定
public class JobExecutor implements Job {
    
    private final JobHandlerRegistry jobHandlerRegistry;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 创建任务执行上下文
        JobExecuteContext jobContext = new JobExecuteContext(context);
        
        String handlerName = context.getJobDetail().getJobDataMap().getString("handlerName");
        if (handlerName == null || handlerName.trim().isEmpty()) {
            String errorMsg = "任务处理器名称为空";
            log.error("任务[{}]执行失败: {}", jobContext.getJobName(), errorMsg);
            throw new JobExecutionException(errorMsg);
        }
        
        // 获取任务处理器
        AbstractJobHandler handler = jobHandlerRegistry.getHandler(handlerName);
        if (handler == null) {
            String errorMsg = "未找到任务处理器: " + handlerName;
            log.error("任务[{}]执行失败: {}", jobContext.getJobName(), errorMsg);
            throw new JobExecutionException(errorMsg);
        }
        
        // 检查并发控制
        if (!isConcurrentAllowed(handler, context)) {
            String errorMsg = "任务不允许并发执行，跳过本次执行";
            log.warn("任务[{}]: {}", jobContext.getJobName(), errorMsg);
            return;
        }
        
        boolean success = false;
        long startTime = System.currentTimeMillis();
        
        try {
            // 执行前置处理
            handler.beforeExecute(jobContext);
            
            // 执行任务
            handler.execute(jobContext);
            
            success = true;
            long costTime = System.currentTimeMillis() - startTime;
            log.info("任务[{}]执行成功，耗时: {}ms", jobContext.getJobName(), costTime);
            
        } catch (Exception e) {
            long costTime = System.currentTimeMillis() - startTime;
            log.error("任务[{}]执行失败，耗时: {}ms", jobContext.getJobName(), costTime, e);
            
            // 调用异常处理
            handler.onException(jobContext, e);
            
            // 抛出JobExecutionException让Quartz知道任务执行失败
            throw new JobExecutionException(e);
            
        } finally {
            // 执行后置处理
            handler.afterExecute(jobContext, success);
        }
    }
    
    /**
     * 检查是否允许并发执行
     *
     * @param handler 任务处理器
     * @param context Quartz执行上下文
     * @return true-允许执行，false-不允许执行
     */
    private boolean isConcurrentAllowed(AbstractJobHandler handler, JobExecutionContext context) {
        // 检查处理器类是否标注了@DisallowConcurrent注解
        if (handler.getClass().isAnnotationPresent(DisallowConcurrent.class)) {
            return !isJobCurrentlyRunning(context);
        }
        
        // 检查数据库配置的并发控制
        Boolean concurrent = context.getJobDetail().getJobDataMap().getBoolean("concurrent");
        if (concurrent != null && !concurrent) {
            return !isJobCurrentlyRunning(context);
        }
        
        return true;
    }
    
    /**
     * 检查任务是否正在运行
     *
     * @param context Quartz执行上下文
     * @return true-正在运行，false-未运行
     */
    private boolean isJobCurrentlyRunning(JobExecutionContext context) {
        try {
            // 获取当前正在执行的任务
            return context.getScheduler().getCurrentlyExecutingJobs().stream()
                    .anyMatch(executingContext -> 
                            executingContext.getJobDetail().getKey().equals(context.getJobDetail().getKey()) &&
                            !executingContext.equals(context));
        } catch (Exception e) {
            log.warn("检查任务并发状态失败", e);
            return false;
        }
    }
}