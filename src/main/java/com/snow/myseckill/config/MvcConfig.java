package com.snow.myseckill.config;

import com.snow.myseckill.interceptor.MvcInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                        new MvcInterceptor())
                .addPathPatterns("/*")
                .excludePathPatterns("/user/login", "/user/logout", "/user/register")
                .excludePathPatterns("/favicon.ico", "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    public MvcInterceptor mvcInterceptor() {
        return new MvcInterceptor();
    }

}
