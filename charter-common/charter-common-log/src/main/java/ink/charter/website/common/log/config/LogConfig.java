package ink.charter.website.common.log.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 日志配置类
 * 配置异步执行器和启用AOP
 *
 * @author charter
 * @create 2025/07/17
 */
@Configuration
@EnableAsync
@EnableAspectJAutoProxy
public class LogConfig {

    /**
     * 配置异步执行器，用于异步记录日志
     */
    @Bean("logTaskExecutor")
    public Executor logTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数
        executor.setCorePoolSize(2);
        
        // 最大线程数
        executor.setMaxPoolSize(5);
        
        // 队列容量
        executor.setQueueCapacity(100);
        
        // 线程名前缀
        executor.setThreadNamePrefix("log-async-");
        
        // 线程空闲时间（秒）
        executor.setKeepAliveSeconds(60);
        
        // 拒绝策略：由调用线程执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // 等待时间（秒）
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        return executor;
    }
    
    /**
     * 配置RestTemplate，用于IP地址查询等HTTP请求
     */
    @Bean("logRestTemplate")
    public RestTemplate logRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 设置连接超时时间为3秒
        restTemplate.getRequestFactory();
        return restTemplate;
    }
    

}