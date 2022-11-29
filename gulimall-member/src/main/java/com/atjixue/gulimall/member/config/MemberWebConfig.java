package com.atjixue.gulimall.member.config;

import com.atjixue.gulimall.member.interceptor.LoginUserInterceptor;
import io.renren.modules.app.config.WebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author LiuJixue
 * @Date 2022/9/22 16:11
 * @PackageName:com.atjixue.gulimall.member.config
 * @ClassName: MemberWebConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class MemberWebConfig implements WebMvcConfigurer {

    @Autowired
    LoginUserInterceptor loginUserInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**");
    }
}
