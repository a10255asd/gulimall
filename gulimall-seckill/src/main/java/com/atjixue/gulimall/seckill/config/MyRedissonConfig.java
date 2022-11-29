package com.atjixue.gulimall.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @Author LiuJixue
 * @Date 2022/8/22 15:50
 * @PackageName:com.atjixue.gulimall.product.config
 * @ClassName: MyRedissonConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class MyRedissonConfig {
    /**
     * 所有对Redisson的使用都要通过 RedissonClient 对象
     * */
    @Bean(destroyMethod="shutdown")
    RedissonClient redisson() throws IOException {
        // 创建配置
        Config config = new Config();
        // Redis url should start with redis:// or rediss://
        config.useSingleServer().setAddress("redis://0.0.0.0:6379");
        // 根据config对象创建出RedissonClient的实例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
