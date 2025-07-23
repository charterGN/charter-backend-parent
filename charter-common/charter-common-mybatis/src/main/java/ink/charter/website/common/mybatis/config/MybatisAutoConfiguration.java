package ink.charter.website.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import ink.charter.website.common.mybatis.handler.CustomMetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis-Plus自动配置类
 * 自动装配MyBatis-Plus相关组件
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(MybatisPlusInterceptor.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
@Import({MybatisPlusConfig.class})
@MapperScan(value = "ink.charter.website", annotationClass = Mapper.class)
@EnableTransactionManagement(proxyTargetClass = true)   // 开启事务管理
public class MybatisAutoConfiguration {

    /**
     * 注册MyBatis-Plus拦截器
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        log.info("初始化MyBatis-Plus拦截器");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 添加分页拦截器
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求，默认false
        paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认500条，-1不受限制
        paginationInterceptor.setMaxLimit(500L);
        // 开启count的join优化，只针对部分left join
        paginationInterceptor.setOptimizeJoin(true);
        
        interceptor.addInnerInterceptor(paginationInterceptor);
        
        return interceptor;
    }

    /**
     * 注册自动填充处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler() {
        log.info("初始化自动填充处理器");
        return new CustomMetaObjectHandler();
    }
}