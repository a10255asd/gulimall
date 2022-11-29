package com.atjixue.gulimall.seckill;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 * 整合sentinel
 * 1）引入依赖
 * 2）下载sentinel的控制台
 * 3）、配置sentinel地址信息
 * 4)、在控制台调整参数，默认所有的流控设置保存在内存中，重启失效
 * 每一个微服务都倒入
 * */
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		DruidDataSourceAutoConfigure.class ,
		HibernateJpaAutoConfiguration.class})
@EnableDiscoveryClient()
@EnableFeignClients
public class GulimallSeckillApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallSeckillApplication.class, args);
	}

}
