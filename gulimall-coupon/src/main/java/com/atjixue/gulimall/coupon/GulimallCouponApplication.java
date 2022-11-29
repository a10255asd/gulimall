package com.atjixue.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 *
 * 如何使用nacos作为配置中心同一管理配置
 * 1、引入依赖
 * <dependency>
 *     <groupId>com.alibaba.cloud</groupId>
 *     <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
 * </dependency>
 * 2、创建bootstrap.properties配置文件
 * 配置：
 *      spring.application.name=gulimall-coupon
 * spring.cloud.nacos.config.server-addr=0.0.0.0:8848
 * 3、需要给配置中心默认添加一个叫 数据集，DataID，默认规则：应用名.properties
 * 4、添加任何配置
 * 5、动态获取配置
 * @RefreshScope
 * @Value("${配置项名字}")
 * 两个注解可以动态获取并刷新配置
 * 6、如果配置中心和本地配置文件都配置了相同的项，优先使用配置中心的配置。
 * 二、细节：
 * 命名空间：是来做配置隔离的，可以在控制台看到，默认是public（保留空间），作用是 默认新增的所有都在public下
 * 来发、测试、生产 都会有不同的配置。可以使用不同的命名空间。比如dev（开发环境）、test（测试环境）、prod（生产环境）
 * 需要在bootstrap.properties配置使用哪个命名空间的配置
 * spring.cloud.nacos.config.namespace=04766627-ec42-41d9-8c54-8c6d19602e1f
 * 也可以基于每一个微服务之间进行互相隔离配置，可以每一个微服务都创建自己的命名空间，这样只加载自己命名空间下的配置。
 * 配置集：一组相关或者不想关配置项的集合。所有的配置的集合
 * 配置集ID：类似于配置文件名
 * 配置分组：
 * 默认所有的配置集都属于DEFAULT分组，可以双11的时候用一组配置，双12的时候一组配置。
 *
 * 每个微服务创建自己的命名空间吗，使用配置分组来区分它的环境，比如区分dev和test。
 *3 同时加载多个配置集
 * 1微服务任何配置信息，任何配置文件都可以放在配置中心中
 * 2、只需要在bootstrap.properties说明加载配置中心中哪些配置文件即可
 * 3、@value @ configrationProperties以前springboot任何方式从配置文件中获取值的注解 都能使用
 * 配置中心有的优先使用配置中心的配置
 * */
@SpringBootApplication
@MapperScan("com.atjixue.gulimall.coupon.dao")
@EnableDiscoveryClient
public class GulimallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallCouponApplication.class, args);
    }

}
