package ink.charter.website.common.job.annotation;

import java.lang.annotation.*;

/**
 * 禁止并发执行注解
 * 标识该任务不允许并发执行
 *
 * @author charter
 * @create 2025/07/30
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisallowConcurrent {
}