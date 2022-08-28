package com.stellar.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha Producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.image.width", "120");
        properties.put("kaptcha.image.height", "40");
        properties.put("kaptcha.textproducer.font.size", "30");  // 字号
        properties.put("kaptcha.textproducer.font.color", "black");  // 字体颜色
        properties.put("kaptcha.textproducer.char.space", "4");  // 验证码数量

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}

