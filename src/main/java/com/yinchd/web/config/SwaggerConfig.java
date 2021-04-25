package com.yinchd.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置
 */
@Configuration
public class SwaggerConfig {

    /**
     * 控制swagger是否开启，开关在application.yml文件中，
     * 上线后可以在配置文件中将开关改为false，这样线上就不会生成swagger文档了
     */
    @Value("${web.swagger.enable}")
    private final boolean swaggerEnabled = true;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(swaggerEnabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yinchd.web"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("WebBoot统一API服务")
                .contact(new Contact("yinchd", "#", "sayc@foxmail.com"))
                .version("v1.0")
                .build();
    }

}