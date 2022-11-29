package com.atjixue.gulimall.order.vo;

import lombok.Data;

/**
 * @Author LiuJixue
 * @Date 2022/9/7 14:44
 * @PackageName:com.atjixue.gulimall.order.vo
 * @ClassName: SkuStockVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class SkuStockVo {
    private Long skuId;
    private Boolean hasStock;
}
