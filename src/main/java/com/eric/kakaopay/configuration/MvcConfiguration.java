package com.eric.kakaopay.configuration;

import com.eric.kakaopay.interceptor.RequiredValueCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequiredValueCheckInterceptor())
                .excludePathPatterns("/resources/**");
    }
}
