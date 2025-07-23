package ink.charter.website.common.web.config;

import ink.charter.website.common.web.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Web自动配置类
 * 用于自动配置Web相关的组件
 *
 * @author charter
 * @create 2025/01/20
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(DispatcherServlet.class)
@Import({CharterWebConfig.class, Knife4jConfig.class})
public class WebAutoConfiguration {

    /**
     * 全局异常处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        log.info("[WebAutoConfiguration] 初始化全局异常处理器");
        return new GlobalExceptionHandler();
    }

    static {
        log.info("[WebAutoConfiguration] Web自动配置类已加载");
    }
}