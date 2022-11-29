package com.atjixue.gulimall.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author LiuJixue
 * @Date 2022/9/4 23:32
 * @PackageName:com.atjixue.gulimall.order.config
 * @ClassName: MyRabbitConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class MyRabbitConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    /**
     * 定制RabbitTemplate
     * 1、spring.rabbitmq.publisher-confirms=true
     * 设置确认回调
     * */
    @PostConstruct  //PostConstruct注解的作用 MyRabbitConfig对象呢创建完成后，执行这个放啊
    public void initRabbitTemplate(){
        // 设置确认会带哦
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * @param correlationData 当前消息的唯一关联数据（这是消息的唯一id）
             * @param ack 消息是否成功收到
             * @param s 失败原因
             * 1、只要消息抵达broker ack 等于true
             * 消息正确抵达队列进行回掉
             *2、消息正确抵达进行回调
             *          spring.rabbitmq.publisher-returns=true
             * # 只要抵达队列，以异步方式优先回调returnconfirm
             * spring.rabbitmq.template.mandatory=true
             * 3、消费端确认（保证每个消息被正确消费，此时才可以broker删除这个消息）
             *          默认是自动确认的 只要消息接收到 客户端会自动确认 服务端就会移除这个消息
             *          问题：收到很多消息，自动回复给服务器ack 只有一个消息处理成功 宕机了 就会发生消息丢失
             *          消费者手动确认 只要我们没有明确告诉mq货物被签收，没有ack 消息就一直是unacked状态，即使服务器宕机，
             *          消息不会丢失，会重置为ready状态，下一次有新的consumer链接进来就发给他
             *4、如何签收
             * */
            /**
             * 做好消息确认机制（publisher，consumer 手动ack）
             * 每一个发送的消息都在数据库做好记录，定期将失败的消息再次发送一遍
             * */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String s) {
                System.out.println("confirm...correlationData【"+correlationData+"】===》b[" + ack + "==>]s[" + s + "]");
            }
        });
        // 只要消息没有投递给指定的队列，就处罚这个失败回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * @param message 投递失败的消息详细信息
             * @param replyCode 回复的状态码
             * @param replyText 回复的文本内容
             * @param exchange 当时这个消息发给哪个交换机、】】】】】】】
             * @param routingKey 当时这个消息用哪个路由键
             * */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                // 爆错误了，修改数据库，当前消息的错误状态
                System.out.println("Fail Message[" +message+"]===>["+replyCode+"]===>exchange["+exchange+"]routingKey[" + routingKey+"]");
            }
        });
    }
}
