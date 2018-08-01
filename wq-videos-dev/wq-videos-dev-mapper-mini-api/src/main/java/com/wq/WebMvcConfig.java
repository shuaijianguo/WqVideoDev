package com.wq;

import com.wq.interceptors.MiniapiInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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

    @Bean
    public MiniapiInterceptor miniapiInterceptor(){
        return new MiniapiInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //把自己的定义的拦截器添加到拦截器注册 中心
        registry.addInterceptor(miniapiInterceptor()).addPathPatterns("/user/**")
        .addPathPatterns("/bgm/**")
        .addPathPatterns("/video/upload","/video/uploadCover");//视频查看全部不需要拦截所以不需要写在这里
        super.addInterceptors(registry);
    }
}
