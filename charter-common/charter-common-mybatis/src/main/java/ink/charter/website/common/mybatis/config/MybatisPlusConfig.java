package ink.charter.website.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 * 用于配置MyBatis-Plus相关的拦截器和插件
 *
 * @author charter
 * @create 2025/07/19
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 配置MyBatis-Plus拦截器
     * 包含分页拦截器等
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
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
}