package com.atjixue.gulimall.seckill.to;

import com.atjixue.gulimall.seckill.vo.SeckillSkuVo;
import com.atjixue.gulimall.seckill.vo.SkuInfoVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/25 23:07
 * @PackageName:com.atjixue.gulimall.seckill.to
 * @ClassName: SecKillSkuRedisto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class SecKillSkuRedisTo {
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

    //当前商品秒杀的开始时间
    private Long startTime;

    //当前商品秒杀的结束时间
    private Long endTime;

    //当前商品秒杀的随机码
    private String randomCode;

    //sku的详细信息
    private SkuInfoVo skuInfo;
}
