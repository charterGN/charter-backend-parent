package ink.charter.website.common.job.config;

import ink.charter.website.common.job.core.JobExecutor;
import ink.charter.website.common.job.core.JobManager;
import ink.charter.website.common.job.handler.JobHandlerRegistry;
import ink.charter.website.common.job.listener.JobListener;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 定时任务自动配置类
 * 自动配置定时任务相关的Bean
 *
 * @author charter
 * @create 2025/07/30
 */
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(name = "charter.job.enabled", havingValue = "true", matchIfMissing = true)
@Import({QuartzConfig.class})
public class JobAutoConfiguration {
    
    public JobAutoConfiguration() {
        log.info("Charter定时任务框架自动配置启动");
    }
    
    /**
     * 任务处理器注册中心
     */
    @Bean
    public JobHandlerRegistry jobHandlerRegistry() {
        return new JobHandlerRegistry();
    }
    
    /**
     * 任务执行器
     */
    @Bean
    public JobExecutor jobExecutor(JobHandlerRegistry jobHandlerRegistry) {
        return new JobExecutor(jobHandlerRegistry);
    }
    
    /**
     * 任务管理器
     */
    @Bean
    public JobManager jobManager(Scheduler scheduler) {
        return new JobManager(scheduler);
    }
    
    /**
     * 任务监听器
     */
    @Bean
    public JobListener jobListener() {
        return new JobListener();
    }
}