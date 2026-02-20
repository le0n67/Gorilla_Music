package com.gorillamusic.interceptor;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Date：2026/2/20  13:04
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */


@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    @Resource
    private AppInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }
}

















