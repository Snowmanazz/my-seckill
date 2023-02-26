package com.snow.myseckill.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configurable
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("秒杀案例").apiInfo(apiInfo());
    }
    // 预览地址:swagger-ui.html
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Seckill的Swagger2构建文档")
                .contact("SnowMan")
                .version("1.1")
                .build();
    }
}
