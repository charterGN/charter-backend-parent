package ink.charter.website.common.job.handler;

import ink.charter.website.common.job.core.JobExecuteContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象任务处理器
 * 所有定时任务处理器都应该继承此类
 *
 * @author charter
 * @create 2025/07/30
 */
@Slf4j
public abstract class AbstractJobHandler {
    
    /**
     * 执行任务
     * 子类必须实现此方法来定义具体的任务逻辑
     *
     * @param context 任务执行上下文
     * @throws Exception 任务执行异常
     */
    public abstract void execute(JobExecuteContext context) throws Exception;
    
    /**
     * 任务执行前的准备工作
     * 子类可以重写此方法来实现自定义的前置处理
     *
     * @param context 任务执行上下文
     */
    public void beforeExecute(JobExecuteContext context) {
        log.info("任务[{}]开始执行，参数: {}", context.getJobName(), context.getParams());
    }
    
    /**
     * 任务执行后的清理工作
     * 子类可以重写此方法来实现自定义的后置处理
     *
     * @param context 任务执行上下文
     * @param success 是否执行成功
     */
    public void afterExecute(JobExecuteContext context, boolean success) {
        if (success) {
            log.info("任务[{}]执行完成", context.getJobName());
        } else {
            log.error("任务[{}]执行失败", context.getJobName());
        }
    }
    
    /**
     * 任务执行异常处理
     * 子类可以重写此方法来实现自定义的异常处理逻辑
     *
     * @param context 任务执行上下文
     * @param exception 异常信息
     */
    public void onException(JobExecuteContext context, Exception exception) {
        log.error("任务[{}]执行异常", context.getJobName(), exception);
    }
    
    /**
     * 获取任务处理器名称
     * 默认返回类的简单名称
     *
     * @return 处理器名称
     */
    public String getHandlerName() {
        return this.getClass().getSimpleName();
    }
    
    /**
     * 获取任务描述
     * 子类可以重写此方法来提供任务描述
     *
     * @return 任务描述
     */
    public String getDescription() {
        return "定时任务处理器";
    }
    
    /**
     * 是否支持并发执行
     * 默认支持并发执行，子类可以重写此方法
     *
     * @return true-支持并发，false-不支持并发
     */
    public boolean supportConcurrent() {
        return true;
    }
}