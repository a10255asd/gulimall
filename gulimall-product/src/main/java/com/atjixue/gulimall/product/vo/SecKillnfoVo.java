package com.atjixue.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author LiuJixue
 * @Date 2022/9/27 00:09
 * @PackageName:com.atjixue.gulimall.product.vo
 * @ClassName: SecKillInfoVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class SecKillnfoVo {
    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private Integer seckillCount;
    /**
     * 每人限购数量
     */
    private Integer seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

}
