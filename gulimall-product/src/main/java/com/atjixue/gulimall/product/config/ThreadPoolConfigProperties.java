package com.atjixue.gulimall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author LiuJixue
 * @Date 2022/8/29 14:55
 * @PackageName:com.atjixue.gulimall.product.config
 * @ClassName: ThreadPoolConfigProperties
 * @Description: TODO
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "gulimall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAAliveTime;
}
