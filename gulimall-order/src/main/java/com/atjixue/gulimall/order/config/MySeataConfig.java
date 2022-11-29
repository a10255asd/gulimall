package com.atjixue.gulimall.order.config;

import com.zaxxer.hikari.HikariDataSource;



//import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @Author LiuJixue
 * @Date 2022/9/14 23:34
 * @PackageName:com.atjixue.gulimall.order.config
 * @ClassName: MySeataConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class MySeataConfig {
//    @Autowired
//    DataSourceProperties dataSourceProperties;
//    @Bean
//    public DataSource dataSource(DataSourceProperties dataSourceProperties){
//        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
//        if(StringUtils.hasText(dataSourceProperties.getName())){
//            dataSource.setPoolName(dataSourceProperties.getName());
//        }
//        return new DataSourceProxy(dataSource);
//    }
}
