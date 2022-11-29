package com.atjixue.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author LiuJixue
 * @Date 2022/9/8 17:23
 * @PackageName:com.atjixue.gulimall.order.vo
 * @ClassName: FareVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class FareVo {
    private MemberAddressVo address;
    private BigDecimal fare;
}
