package com.atjixue.gulimall.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author LiuJixue
 * @Date 2022/8/23 15:03
 * @PackageName:com.atjixue.gulimall.product.config
 * @ClassName: MyCacheConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties({CacheProperties.class})
public class MyCacheConfig {
    /**
     * 配置文件中的东西没有用上
     * 原来和配置文件绑定的配置类是这样的
     * @ConfigurationProperties(
     *     prefix = "spring.cache"
     * )
     * public class CacheProperties {
     * 要让它生效,使用@EnableConfigurationProperties注解,开启绑定@EnableConfigurationProperties({CacheProperties.class})
     *
     * */

    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties){
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        //config = config.entryTtl();
        config =config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        config =config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        // 将配置文件中的所有东西都生效
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if(redisProperties.getTimeToLive()!=null){
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if(redisProperties.getKeyPrefix()!=null){
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if(!redisProperties.isCacheNullValues()){
            config = config.disableCachingNullValues();
        }
        if(!redisProperties.isUseKeyPrefix()){
            config = config.disableKeyPrefix();
        }
        return config;
    }

}
