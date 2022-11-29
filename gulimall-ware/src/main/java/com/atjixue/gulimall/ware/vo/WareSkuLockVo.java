package com.atjixue.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/10 17:24
 * @PackageName:com.atjixue.gulimall.order.vo
 * @ClassName: WareSkuLockVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class WareSkuLockVo {
    //订单号
    private String orderSn;
    // 需要锁住的所有信息
    private List<OrderItemVo> locks;
    
}
