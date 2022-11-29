package com.atjixue.gulimall.search;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 *  1）、导入依赖
 *  <dependency>
 *             <groupId>org.elasticsearch.client</groupId>
 *             <artifactId>elasticsearch-rest-high-level-client</artifactId>
 *             <version>7.4.2</version>
 *         </dependency>
 *  2）、编写配置，给容器中注入 RestHighLevelClient
 *  3）、
 *
 * {
 *     skuid:1
 *     skutitle:华为
 *     price:1113
 *     saleCount:99
 *     attrs:{
 *         {尺寸：5村},
 *          {cup:高通945}
 *     }
 * }
 * 冗余
 *  100万 * 20 = 1000000 * 2k = 2000MB = 2g
 * (2)、
 * {
 * sku索引{
 *      skuid:1,
 *      spuid:11
 *      xxx
 * }
 * }
 * */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class ,
        HibernateJpaAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients //开启远程调用
@EnableRedisHttpSession
public class GulimallSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallSearchApplication.class, args);
    }

}
