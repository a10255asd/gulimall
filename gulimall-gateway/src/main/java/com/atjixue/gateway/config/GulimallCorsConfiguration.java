package com.atjixue.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @Author LiuJixue
 * @Date 2022/8/4 10:49
 * @PackageName:com.atjixue.gateway.config
 * @ClassName: CorsConfiguration
 * @Version 1.0
 */
@Configuration
public class GulimallCorsConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
        CorsConfiguration CorsConfiguration = new CorsConfiguration();
        // 1、配置跨域
        CorsConfiguration.addAllowedHeader("*");
        CorsConfiguration.addAllowedMethod("*");
        CorsConfiguration.addAllowedOrigin("*");
        CorsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**",CorsConfiguration);
        return new CorsWebFilter(source);

    }
}
