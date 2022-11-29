package com.atjixue.gulimall.order.to;

import com.atjixue.gulimall.order.entity.OrderEntity;
import com.atjixue.gulimall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/8 16:43
 * @PackageName:com.atjixue.gulimall.order.to
 * @ClassName: OrderCreateTo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class OrderCreateTo {
    private OrderEntity order;

    private List<OrderItemEntity> orderItems;

    private BigDecimal payPrice; //计算的应付价格

    private BigDecimal fare;



}
