package com.atjixue.gulimall.order.controller;

import com.atjixue.gulimall.order.entity.OrderEntity;
import com.atjixue.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @Author LiuJixue
 * @Date 2022/9/5 21:36
 * @PackageName:com.atjixue.gulimall.order.controller
 * @ClassName: RabbitController
 * @Description: TODO
 * @Version 1.0
 */
@RestController
@Slf4j
public class RabbitController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMq")
    public String sendMessageTest(@RequestParam(value="num",defaultValue = "10") Integer num){
        for(int i = 0;i<num;i++){
            if(i%2==0) {
                OrderReturnReasonEntity orderReturnReasonEntity = new OrderReturnReasonEntity();
                orderReturnReasonEntity.setId(1L);
                orderReturnReasonEntity.setCreateTime(new Date());
                orderReturnReasonEntity.setName("haha" + i);
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", orderReturnReasonEntity);
                log.info("消息发送完成{}", orderReturnReasonEntity);
            }else {
                OrderEntity entity = new OrderEntity();
                entity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello22.java", entity);
            }
        }
        return "ok";
    }
}
