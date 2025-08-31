package ink.charter.website.common.job.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Quartz配置类
 * 配置Quartz调度器的相关属性
 *
 * @author charter
 * @create 2025/07/30
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "charter.job.enabled", havingValue = "true", matchIfMissing = true)
public class QuartzConfig {
    
    /**
     * 配置SchedulerFactoryBean
     *
     * @param dataSource 数据源
     * @return SchedulerFactoryBean
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        
        // 设置数据源
        factory.setDataSource(dataSource);
        
        // 设置Quartz属性
        factory.setQuartzProperties(quartzProperties());
        
        // 设置调度器名称
        factory.setSchedulerName("CharterScheduler");
        
        // 应用启动完成后启动调度器
        factory.setStartupDelay(10);
        
        // 应用关闭时等待任务完成
        factory.setWaitForJobsToCompleteOnShutdown(true);
        
        // 覆盖已存在的任务
        factory.setOverwriteExistingJobs(true);
        
        // 自动启动
        factory.setAutoStartup(true);
        
        log.info("Quartz调度器配置完成");
        
        return factory;
    }
    
    /**
     * 获取Scheduler实例
     *
     * @param schedulerFactoryBean SchedulerFactoryBean
     * @return Scheduler
     */
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
        return schedulerFactoryBean.getScheduler();
    }
    
    /**
     * 配置Quartz属性
     *
     * @return Properties
     */
    private Properties quartzProperties() {
        Properties properties = new Properties();
        
        // 调度器属性
        properties.setProperty("org.quartz.scheduler.instanceName", "CharterScheduler");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
        properties.setProperty("org.quartz.scheduler.jmx.export", "true");
        
        // 线程池属性
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount", "20");
        properties.setProperty("org.quartz.threadPool.threadPriority", "5");
        properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
        
        // JobStore属性 - 使用数据库存储
        properties.setProperty("org.quartz.jobStore.class", "org.springframework.scheduling.quartz.LocalDataSourceJobStore");
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        properties.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "20000");
        properties.setProperty("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        properties.setProperty("org.quartz.jobStore.txIsolationLevelSerializable", "false");
        properties.setProperty("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS WHERE SCHED_NAME = {1} AND LOCK_NAME = ? FOR UPDATE");
        
        // 集群属性
        properties.setProperty("org.quartz.jobStore.acquireTriggersWithinLock", "true");
        properties.setProperty("org.quartz.jobStore.lockHandler.class", "org.quartz.impl.jdbcjobstore.UpdateLockRowSemaphore");
        
        // 错过执行策略
        properties.setProperty("org.quartz.jobStore.misfireThreshold", "60000");
        
        log.info("Quartz属性配置完成");
        
        return properties;
    }
}