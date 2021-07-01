package com.yinchd.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 配置管理器
 * @author yinchd
 * @date 2019/11/1
*/
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Resource
    private WebInterceptor webInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webInterceptor).addPathPatterns("/**");
    }

}
