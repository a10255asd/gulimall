package com.atjixue.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 使用rabbitmq
 * 引入amqp场景启动器：RabbitAutoConfiguration就会自动生效
 * 给容器中自动配置了：RabbitTemplate AmqpAdmin CachingConnectionFactory RabbitMessagingTemplate
 * 所有的属性都是在， 以spring.rabbitmq 开头
 * @ConfigurationPropertiter(prefix = "spring.rabbitmq")进行绑定
 * @EnableRabbit: @EnableXXXXX
 *
 * 给配置文件中配置spring.rabbitmq 开头的相关信息
 * 开启：@EnableRabbit
 * 监听消息 @RabbitListener 必须有@EnableRabbit才能监听
 *
 * */
@SpringBootApplication
@MapperScan("com.atjixue.gulimall.order.dao")
@EnableDiscoveryClient
@EnableRabbit
@EnableRedisHttpSession
@EnableFeignClients
@EnableAspectJAutoProxy(exposeProxy = true)
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
