package com.atjixue.gulimall.order.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author LiuJixue
 * @Date 2022/9/7 23:26
 * @PackageName:com.atjixue.gulimall.order.vo
 * @ClassName: OrderSubmitVo
 * @Description: 封装订单提交的数据
 * @Version 1.0
 */
@Data
@ToString
public class OrderSubmitVo {

    private Long addrId;
    private Integer payType;
    // 去须提交需要购买的商品 去购物车在获取一遍
    private String orderToken;//令牌
    private BigDecimal payPrice;// 应付价格 验价
    private String note; // 订单备注
    // 用户相关信息在session里面
}
