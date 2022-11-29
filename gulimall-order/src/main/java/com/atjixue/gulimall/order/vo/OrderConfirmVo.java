package com.atjixue.gulimall.order.vo;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author LiuJixue
 * @Date 2022/9/6 16:59
 * @PackageName:com.atjixue.gulimall.order.vo
 * @ClassName: OrderConfirmVo
 * @Description: 订单需要用的数据
 * @Version 1.0
 */
public class OrderConfirmVo {
    // 收货地址 ums_member_receive_address 表
    @Setter @Getter
    private List<MemberAddressVo> address;
    // 所有选中的购物项
    @Setter @Getter
    private List<OrderItemVo> items;
    // 发票记录。。。
    // 防重令牌
    @Setter @Getter
    private String orderToken;

    public Integer getCount(){
        Integer i = 0;
        if(items !=null){
            for(OrderItemVo item: items){
               i += item.getCount();
            }
        }
        return i;
    }
    // 优惠卷信息
    @Setter @Getter
    private Integer integration;
    // 订单总额
    public BigDecimal getTotal(){
        BigDecimal sum = new BigDecimal("0");
        if(items !=null){
            for(OrderItemVo item: items){
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                sum  = sum.add(multiply);
            }
        }
        return sum;
    }
    // 应付总额


    public BigDecimal getPayPrice() {

        return getTotal();
    }
    @Setter @Getter
    Map<Long,Boolean> stocks;
}
