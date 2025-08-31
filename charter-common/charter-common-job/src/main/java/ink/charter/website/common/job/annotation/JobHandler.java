package ink.charter.website.common.job.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 任务处理器注解
 * 用于标识定时任务处理器类
 *
 * @author charter
 * @create 2025/07/30
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface JobHandler {
    
    /**
     * 任务处理器名称
     * 必须唯一，用于任务配置中的job_class字段
     */
    String value();
    
    /**
     * 任务描述
     */
    String description() default "";
}