package com.yinchd.web.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Validator配置
 * @author yinchd
 * @date 2019/9/20
*/
@Configuration
public class ValidatorConfig {

    /**
     * 捕获取validator异常快速返回失败结果
     * @author yinchd
     * @date 2019/9/20
    */
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
