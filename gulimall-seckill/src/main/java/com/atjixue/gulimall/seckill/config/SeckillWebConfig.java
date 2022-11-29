package com.atjixue.gulimall.seckill.config;

import com.atjixue.gulimall.seckill.interceptor.LoginUserInterceptor;
import io.renren.modules.app.config.WebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author LiuJixue
 * @Date 2022/9/27 14:31
 * @PackageName:com.atjixue.gulimall.seckill.config
 * @ClassName: SeckillWebConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class SeckillWebConfig implements WebMvcConfigurer {
    @Autowired
    LoginUserInterceptor loginUserInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**");
    }
}
