package com.atjixue.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author LiuJixue
 * @Date 2022/9/27 15:32
 * @PackageName:com.atjixue.common.to
 * @ClassName: SeckillOrderTo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class SeckillOrderTo {
    // 订单号
    private String orderSn;
    // 活动场次id
    private Long promotionSessionId;
    // 商品id
    private Long skuId;
    // 秒杀价格
    private BigDecimal seckillPrice;
    // 购买数量
    private Integer num;
    // 会员id
    private Long memberId;


}
