package com.snow.myseckill.config;

import com.snow.myseckill.interceptor.MvcInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    MvcInterceptor mvcInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                        mvcInterceptor())
                .addPathPatterns("/*")
                .excludePathPatterns("/user/login", "/user/logout", "/user/register")
                .excludePathPatterns("/favicon.ico", "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    public MvcInterceptor mvcInterceptor() {
        return mvcInterceptor;
    }

}
