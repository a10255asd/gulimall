package com.atjixue.gulimall.order.web;

import com.atjixue.gulimall.order.entity.OrderEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.UUID;

/**
 * @Author LiuJixue
 * @Date 2022/9/6 12:40
 * @PackageName:com.atjixue.gulimall.order.web
 * @ClassName: HelloController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
public class HelloController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/{page}.html")
    public String listPage(@PathVariable("page") String page){

        return page;
    }

    @GetMapping("/test/CreateOrder")
    public String createOrderTest(){
        // 订单下单成功
        OrderEntity entity = new OrderEntity();
        entity.setOrderSn(UUID.randomUUID().toString());
        entity.setModifyTime(new Date());
        // 给MQ发送消息。
        rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",entity);
        return "ok";
    }
}
