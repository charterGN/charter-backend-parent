package ink.charter.website.common.log.config;

import ink.charter.website.common.log.aspect.OperationLogAspect;
import ink.charter.website.common.log.mapper.SysOptLogMapper;
import ink.charter.website.common.log.service.OperationLogService;
import ink.charter.website.common.log.service.impl.OperationLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 日志自动配置类
 * 自动装配日志相关组件
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@AutoConfiguration
@EnableAsync
@ConditionalOnClass(OperationLogAspect.class)
@Import({LogConfig.class})
public class LogAutoConfiguration {

    /**
     * 注册日志任务执行器
     */
    @Bean("logTaskExecutor")
    @ConditionalOnMissingBean(name = "logTaskExecutor")
    public Executor logTaskExecutor() {
        log.info("初始化日志任务执行器");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数
        executor.setCorePoolSize(2);
        
        // 最大线程数
        executor.setMaxPoolSize(5);
        
        // 队列容量
        executor.setQueueCapacity(100);
        
        // 线程名前缀
        executor.setThreadNamePrefix("log-task-");
        
        // 拒绝策略：由调用线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // 等待时间
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        return executor;
    }

    /**
     * 注册日志专用RestTemplate
     */
    @Bean("logRestTemplate")
    @ConditionalOnMissingBean(name = "logRestTemplate")
    public RestTemplate logRestTemplate() {
        log.info("初始化日志专用RestTemplate");
        RestTemplate restTemplate = new RestTemplate();
        // 设置连接超时时间为3秒
        restTemplate.getRequestFactory();
        return restTemplate;
    }

    /**
     * 注册日志属性配置
     */
    @Bean
    @ConditionalOnMissingBean
    public LogProperties logProperties() {
        log.info("初始化日志属性配置");
        return new LogProperties();
    }

    /**
     * 注册操作日志服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.log", name = "enabled", havingValue = "true", matchIfMissing = true)
    public OperationLogService operationLogService(SysOptLogMapper sysOptLogMapper) {
        log.info("初始化操作日志服务");
        return new OperationLogServiceImpl(sysOptLogMapper);
    }

    /**
     * 注册操作日志切面
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "charter.log", name = "enabled", havingValue = "true", matchIfMissing = true)
    public OperationLogAspect operationLogAspect(OperationLogService operationLogService, LogProperties logProperties) {
        log.info("初始化操作日志切面");
        return new OperationLogAspect(operationLogService, logProperties);
    }
}