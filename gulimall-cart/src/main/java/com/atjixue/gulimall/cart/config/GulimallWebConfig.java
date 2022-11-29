package com.atjixue.gulimall.cart.config;

import com.atjixue.gulimall.cart.interceptor.CartInterceptor;
import io.renren.modules.app.config.WebMvcConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author LiuJixue
 * @Date 2022/9/2 20:42
 * @PackageName:com.atjixue.gulimall.cart.config
 * @ClassName: GulimallWebConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
