package com.yinchd.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

/**
 * Swagger配置
 * @author yinchd
 * @date 2019/9/20
 */
@Configuration
public class SwaggerConfig {

    @Value("${web.swagger.enable}")
    private final boolean swaggerEnabled = true;

    @Bean
    public Docket createRestApi() {
        // 添加统一请求头
        List<RequestParameter> headers = Collections.singletonList(new RequestParameterBuilder()
                .in(ParameterType.HEADER)
                .name("token")
                .required(false)
                .query(q -> q.model(model -> model.scalarModel(ScalarType.STRING)))
                .build());
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(swaggerEnabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yinchd.web"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(headers)
                .pathMapping("/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("人才集团一企一屏项目接口文档")
                .contact(new Contact("yinchd", "#", "yincd@hua-cloud.com.cn"))
                .version("v1.0")
                .build();
    }

}