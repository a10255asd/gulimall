package com.atjixue.gulimall.order;


import com.atjixue.gulimall.order.entity.OrderEntity;
import com.atjixue.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class GulimallOrderApplicationTests {
    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 发送消息
     * */
    @Test
    public void sendMessageTest(){
        // 发送消息 如果发送的消息是个对象 我们会使用序列化机制，将对象鞋出去 必须实现Serializable接口
        String msg = "hello world";


        // 发送的对象类型的消息 可以是一个JSON
        for(int i = 0;i<10;i++){
            if(i%2==0) {
                OrderReturnReasonEntity orderReturnReasonEntity = new OrderReturnReasonEntity();
                orderReturnReasonEntity.setId(1L);
                orderReturnReasonEntity.setCreateTime(new Date());
                orderReturnReasonEntity.setName("hah" + i);
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", orderReturnReasonEntity);
                log.info("消息发送完成{}", orderReturnReasonEntity);
            }else {
                OrderEntity entity = new OrderEntity();
                entity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", entity);
            }
        }
    }

    /**
     * 如何创建exchange queue binding
     * 1。使用AmqpAdmin进行创建
     * 如何收发消息
     * */
    @Test
   public void createExchange() {
        //amqpAdmin
        //Exchange
        //   public DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
        // 全参数构造器
        DirectExchange directExchange = new DirectExchange("hello-java-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange[{}]创建成功" ,"hello-java-exchange");
    }
    @Test
    public void createQueue(){
        //public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        Queue queue = new Queue("hello-java-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("Exchange[{}]创建成功" ,"hello-java-queue");
    }
    @Test
    public void createBinding(){
        //public Binding(String destination, DestinationType destinationType, String exchange, String routingKey, Map<String, Object> arguments)
        // 目的地 目的地类型 交换机 路由键 自定义参数
        // 将exchange指定的交换机和destination目的地进行绑定，使用routkey作为路由键
        Binding binding = new Binding("hello-java-queue",Binding.DestinationType.QUEUE,
                "hello-java-exchange","hello.java",null);
        amqpAdmin.declareBinding(binding);
        log.info("Exchange[{}]创建成功" ,"hello-java-binding");
    }
}
