package com.atjixue.gulimall.auth.config;

import io.renren.modules.app.config.WebMvcConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author LiuJixue
 * @Date 2022/8/29 17:51
 * @PackageName:com.atjixue.gulimall.auth.config
 * @ClassName: GulimallWebConfig
 * @Description: 视图映射
 * @Version 1.0
 */
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/login.html"). setViewName("login");
        registry.addViewController("/reg.html"). setViewName("reg");

    }
}
