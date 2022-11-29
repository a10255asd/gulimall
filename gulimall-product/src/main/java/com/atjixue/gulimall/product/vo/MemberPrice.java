package com.atjixue.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author LiuJixue
 * @Date 2022/8/12 22:28
 * @PackageName:com.atjixue.gulimall.product.vo
 * @ClassName: MemberPrice
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class MemberPrice {
    private Long id;
    private String name;
    private BigDecimal price;
}
