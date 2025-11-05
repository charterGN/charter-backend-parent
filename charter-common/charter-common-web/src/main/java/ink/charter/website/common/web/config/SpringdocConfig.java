package ink.charter.website.common.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Springdoc配置类
 * 用于配置API文档相关设置
 *
 * @author charter
 * @create 2025/07/19
 */
@Configuration
public class SpringdocConfig {

    /**
     * 配置OpenAPI信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Charter Website API")
                        .version("1.0.0")
                        .description("Charter个人网站后端API接口文档")
                        .contact(new Contact()
                                .name("charter")
                                .email("2088694379@qq.com")
                                .url("https://github.com/charterGN"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
