package ink.charter.website.common.mail.config;

import ink.charter.website.common.mail.service.CharterMailService;
import ink.charter.website.common.mail.service.impl.CharterMailServiceImpl;
import ink.charter.website.common.mail.utils.CharterMailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 邮件自动配置类
 * 自动装配邮件相关组件
 *
 * @author charter
 * @create 2025/11/04
 */
@Slf4j
@AutoConfiguration
@EnableAsync
@ConditionalOnClass({JavaMailSender.class, SpringTemplateEngine.class})
public class MailAutoConfiguration {

    /**
     * 注册邮件任务执行器
     */
    @Bean("mailTaskExecutor")
    @ConditionalOnMissingBean(name = "mailTaskExecutor")
    public Executor mailTaskExecutor() {
        log.info("初始化邮件任务执行器");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数
        executor.setCorePoolSize(2);
        
        // 最大线程数
        executor.setMaxPoolSize(5);
        
        // 队列容量
        executor.setQueueCapacity(100);
        
        // 线程名前缀
        executor.setThreadNamePrefix("mail-task-");
        
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
     * 注册邮件属性配置
     */
    @Bean
    @ConditionalOnMissingBean
    public MailProperties mailProperties() {
        log.info("初始化邮件属性配置");
        return new MailProperties();
    }

    /**
     * 注册邮件服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({JavaMailSender.class, SpringTemplateEngine.class})
    @ConditionalOnProperty(prefix = "charter.mail", name = "enabled", havingValue = "true", matchIfMissing = true)
    public CharterMailService mailService(JavaMailSender javaMailSender,
                                          SpringTemplateEngine templateEngine,
                                          MailProperties mailProperties) {
        log.info("初始化邮件服务");
        CharterMailServiceImpl mailService = new CharterMailServiceImpl(javaMailSender, templateEngine, mailProperties);

        log.info("初始化邮件工具类");
        CharterMailUtils.setMailService(mailService);

        return mailService;
    }
}
