package com.atjixue.gulimall.member.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author LiuJixue
 * @Date 2022/9/6 23:26
 * @PackageName:com.atjixue.gulimall.order.config
 * @ClassName: GuliFeignConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class GuliFeignConfig {
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor(){
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //RequestContextHolder 拿到刚进来的请求
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if(requestAttributes != null){
                    System.out.println("requestInterceptor线程"+ Thread.currentThread().getId());
                    HttpServletRequest request = requestAttributes.getRequest();
                    if(request != null){
                        // 同步请求头数据，cookie
                        String cookie = request.getHeader("Cookie");
                        // 给新请求同步了老请求的cookie
                        requestTemplate.header("Cookie",cookie);
                    }
                }
            }
        };
    }
}
