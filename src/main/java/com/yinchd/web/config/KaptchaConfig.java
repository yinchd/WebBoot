package com.yinchd.web.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 图形验证码配置
 * @author yinchd
 * @date 2019/9/20
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "237,247,255");
        properties.put("kaptcha.background.clear.form", "75,177,255");
        properties.put("kaptcha.background.clear.to", "75,177,255");
        properties.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        properties.put("kaptcha.textproducer.char.length", "4");
        properties.put("kaptcha.noise.color", "white");
        properties.put("kaptcha.textproducer.char.space", "5"); // 字符间距
        properties.put("kaptcha.textproducer.font.names", "Arial");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
