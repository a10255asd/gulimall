package com.atjixue.gulimall.order.listener;

import com.atjixue.gulimall.order.entity.OrderEntity;
import com.atjixue.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author LiuJixue
 * @Date 2022/9/18 22:40
 * @PackageName:com.atjixue.gulimall.order.listener
 * @ClassName: OrderCloseListener
 * @Description: TODO
 * @Version 1.0
 */
@Service
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity entity, Channel channel, Message message) throws IOException {
        System.out.println("收到过期的订单信息，准备关闭订单" + entity.getOrderSn());
        try{
            orderService.closeOrder(entity);
            // 手动调用支付宝收单

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }


    }
}
