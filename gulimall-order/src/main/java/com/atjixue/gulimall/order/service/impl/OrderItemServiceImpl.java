package com.atjixue.gulimall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.atjixue.gulimall.order.entity.OrderEntity;
import com.atjixue.gulimall.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.common.utils.Query;

import com.atjixue.gulimall.order.dao.OrderItemDao;
import com.atjixue.gulimall.order.entity.OrderItemEntity;
import com.atjixue.gulimall.order.service.OrderItemService;


@Service("orderItemService")
@RabbitListener(queues = {"hello-java-queue"})
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }
    /**
     * 声明需要监听的队列
     * org.springframework.amqp.core.Message
     * 参数可以写以下类型
     * Message 原生消息详细信息
     * T <发送的消息类型> OrderReturnReasonEntity content
     *
     * Channel channel 当前传输数据的通道
     * queue ：可以很多人来监听，只能有一个能收到消息
     * 场景：
     *      1、订单服务启动多个 同一个消息 只能有一个收到
     *      2、只有一个消息完全处理完成，方法运行结束，我们才可以接收到下一个消息。
     * @RabbitListener 可以标在 类 +  方法 上
     * @RabbitHandler 可以标在方法上(重载区分不同的消息)
     *
     **/
    //@RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void recieveMessage(Message message, OrderReturnReasonEntity content, Channel channel) throws InterruptedException {
        // 拿到的消息头属性信息
        System.out.println("接收到的消息。。。 内容：" + content);
        MessageProperties properties = message.getMessageProperties();
        // 拿到的请求体信息  {"id":1,"name":"hah","sort":null,"status":null,"createTime":1662353110751}
        byte[] body = message.getBody();
        System.out.println("消息处理完成=》" + content.getName());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("deliveryTag===>" + deliveryTag);
        // 签收,非批量
        try {
            if(deliveryTag%2 == 0){
                // 手动签收
                channel.basicAck(deliveryTag,false);
                System.out.println("签收了 货物_" +deliveryTag);
            }else{
                // 退货
                //(long var1, boolean var3)
                //channel.basicReject(deliveryTag,false);
                //(long var1, boolean var3, boolean var4)
                //var4 是否重新入队，发回到mq false 丢弃 true 发回服务器重新入队
                channel.basicNack(deliveryTag,false,true);
                System.out.println("没有签收 货物_" + deliveryTag);
            }
        } catch (IOException e) {
            // 网络中断
            throw new RuntimeException(e);
        }

    }

    @RabbitHandler
    public void recieveMessage2(OrderEntity content) throws InterruptedException {
        // 拿到的消息头属性信息
        System.out.println("接收到的消息。。。 内容：" + content);

    }
}