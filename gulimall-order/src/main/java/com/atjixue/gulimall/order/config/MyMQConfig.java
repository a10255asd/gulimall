package com.atjixue.gulimall.order.config;

import com.atjixue.gulimall.order.entity.OrderEntity;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author LiuJixue
 * @Date 2022/9/17 18:33
 * @PackageName:com.atjixue.gulimall.order.config
 * @ClassName: MyMQConfig
 * @Description:
 * @Version 1.0
 */
@Configuration
public class MyMQConfig {

    // @Bean Binding Queue
    /**
     * @Bean的作用，Binding，Queue，Exchange 都会自动创建（RabbitMq没有的情况）\
     * RabbitMQ只要有。即使@Bean的属性发生变化 也不会覆盖
     * */
    //String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
    @Bean
    public Queue oderDelayQueue(){
        /**
         * x-dead-letter-exchange: order-event-exchange
         * x-dead-letter-routing-key: order.release.order
         * x-message-ttl: 60000
         * */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","order-event-exchange");
        arguments.put("x-dead-letter-routing-key","order.release.order");
        arguments.put("x-message-ttl",60000);
        Queue queue = new Queue("order.delay.queue", true, false, false,arguments);
        return queue;
    }
    @Bean
    public Queue oderReleaseQueue(){
        // order.release.order.queue
        Queue queue = new Queue("order.release.order.queue", true, false, false);
        return queue;
    }
    @Bean
    public Exchange orderEventExchange(){
        //String name, boolean durable, boolean autoDelete, Map<String, Object> arguments
        TopicExchange topicExchange = new TopicExchange("order-event-exchange", true, false);
        return topicExchange;
    }
    @Bean
    public Binding orderCreateOrderBinding(){
        //String destination, DestinationType destinationType, String exchange, String routingKey, Map<String, Object> arguments
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",
                null);
    }
    @Bean
    public Binding orderReleaseOrderBinding(){
        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                null);
    }
    //订单释放直接和库存释放进行绑定
    @Bean
    public Binding orderReleaseOtherBinding(){
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order.#",
                null);
    }
    @Bean
    public Queue orderSeckillOrderQueue(){
        return new Queue("order.seckill.order.queeue",true,false,false);
    }

    public Binding orderSeckillOrderQueueBinding(){
        return new Binding("order.seckill.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.seckill.order",
                null);
    }
}
