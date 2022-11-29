package com.atjixue.gulimall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author LiuJixue
 * @Date 2022/9/7 17:54
 * @PackageName:com.atjixue.gulimall.ware.vo
 * @ClassName: FareVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class FareVo {
    private MemberAddressVo address;
    private BigDecimal fare;
}
