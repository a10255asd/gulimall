package com.atjixue.gulimall.ware.listener;

import com.alibaba.fastjson.TypeReference;
import com.atjixue.common.to.StockDetailTo;
import com.atjixue.common.to.mq.OrderTo;
import com.atjixue.common.to.mq.StockLockedTo;
import com.atjixue.common.utils.R;
import com.atjixue.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.atjixue.gulimall.ware.entity.WareOrderTaskEntity;
import com.atjixue.gulimall.ware.service.WareSkuService;
import com.atjixue.gulimall.ware.vo.OrderVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author LiuJixue
 * @Date 2022/9/18 21:35
 * @PackageName:com.atjixue.gulimall.ware.listener
 * @ClassName: StockReleaseListener
 * @Description: TODO
 * @Version 1.0
 */
@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {
    @Autowired
    WareSkuService wareSkuService;
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        System.out.println("收到解锁消息。。。");
        try{
            // 当前消息是否被第二次及以后（重新派发过来的）
            //Boolean redelivered = message.getMessageProperties().getRedelivered();
            wareSkuService.unlockStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            System.out.println(e.getMessage());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
    @RabbitHandler
    public void handleOrderCloseRelease(OrderTo orderTo,Message message, Channel channel) throws IOException {
        System.out.println("订单关闭，准备解锁库存。。。");
        try {
            wareSkuService.unlockStock(orderTo);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }
}
