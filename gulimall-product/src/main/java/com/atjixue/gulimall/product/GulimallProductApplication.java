package com.atjixue.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 1. 整合mybatis-plus
 *      1）导入依赖
 *       <dependency>
 *             <groupId>com.baomidou</groupId>
 *             <artifactId>mybatis-plus-boot-starter</artifactId>
 *             <version>3.3.1</version>
 *         </dependency>
 *      2）配置
 *        1、配置数据源
 *          1）、导入数据库的驱动
 *          2）配置数据源 application.yml配置 相关信息
 *        2、配置mybatis -plus
 *          1）使用@MapperScan
 *          2）告诉mybatis-puls sql映射文件位置
 * 2、使用逻辑删除
 *  1）配置全局的逻辑删除规则
 *        logic-delete-value: 1
 *       logic-not-delete-value: 0
 *  2）低版本需要配置逻辑删除的组件
 *  前两步都是可以省略的。
 *  3）加上逻辑删除注解，给bean上，在逻辑删除字段添加@tableLogic注解
 *  3、JSR303
 *  1）、给bean添加校验注解 package javax.validation.constraints
 *  2)、添加校验注解，并添加自己的message提示
 *  3）开启校验功能 添加@Valid注解
 *  4) 给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
 * 4、统一的异常处理
 * @ControllerAdvice功能
 * 1）、编写异常处理类 使用@ControllerAdvice
 * 2）、使用 @ExceptionHandler注解可以处理异常
 * 5、分组校验
 * 给校验注解标注什么情况需要进行校验 ，使用group属性
 * @NotNull(message = "修改必须指定品牌id",groups = {UpdateGroup.class})
 *        @Null(message = "新增不能指定id",groups = {AddGroup.class})
 *    @TableId()
 *    在controller 使用Validated注解,默认没有指定分组的校验注解，在分组校验情况下，不生效。可以完成多场景的复杂校验
 *
 * 6、自定义校验功能
 * 1）、编写自定义校验注解
 * 2）、编写自定义校验器
 * 3）、关联自定义注解和自定义校验器
 * @Documented
 * @Constraint(
 *         validatedBy = {ListValueConstrainValidator.class【可以指定多个不同的校验器，适配不同类型的校验】}
 * )
 * @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
 * @Retention(RetentionPolicy.RUNTIME)
 *
 * 7、页面开发 引入模板引擎 thyleaf-starter 关闭缓存
 *    静态资源放在static 文件夹下，就可以按照路径直接访问
 *    页面放在template下 直接访问
 *    springboot 访问项目的时候，默认会找index
 * 8、页面修改实时更新，不重启服务器的情况洗
 * 1）、引入dev -tools
 * 2)、使用control shift + f9重新编译页面、代码配置推荐重启
 * */
@MapperScan("com.atjixue.gulimall.product.dao")
@SpringBootApplication
@EnableRedisHttpSession
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.atjixue.gulimall.product.feign")
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
