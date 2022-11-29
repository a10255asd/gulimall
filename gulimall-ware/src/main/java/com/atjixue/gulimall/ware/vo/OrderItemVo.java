package com.atjixue.gulimall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/6 17:14
 * @PackageName:com.atjixue.gulimall.order.vo
 * @ClassName: OrderItemVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class OrderItemVo {

    private Long skuId;
    private Boolean check;
    private String title;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;

    private BigDecimal weight;


}
