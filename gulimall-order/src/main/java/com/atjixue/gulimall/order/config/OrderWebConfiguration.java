package com.atjixue.gulimall.order.config;

import com.atjixue.gulimall.order.interceptor.LoginUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author LiuJixue
 * @Date 2022/9/6 16:36
 * @PackageName:com.atjixue.gulimall.order.config
 * @ClassName: OrderWebConfiguration
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class OrderWebConfiguration implements WebMvcConfigurer {
    @Autowired
    LoginUserInterceptor loginUserInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**");
    }
}
