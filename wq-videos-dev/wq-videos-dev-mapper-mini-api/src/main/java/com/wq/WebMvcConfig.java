package com.wq;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by wuqingvika on 2018/7/7.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")//swagger-ui是放在静态目录下的
				    // .addResourceLocations("file:D:/wqlesson/userData/");//本地虚拟目录映射
        .addResourceLocations("file:E:\\wuqingGitHub\\userData\\");
        
        //http://localhost:8081/180701HKMX8GA2CH/face/wx5dadb7c2be0c857b.o6zAJsyRSIo7y30m1iGp-km6Q2mE.7xQUJEzkGG3pa1cb19e6ef75b3330dfe5c378ce13967.jpg
    }
}
