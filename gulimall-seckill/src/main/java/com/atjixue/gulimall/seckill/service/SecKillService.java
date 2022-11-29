package com.atjixue.gulimall.seckill.service;

import com.atjixue.gulimall.seckill.to.SecKillSkuRedisTo;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/25 21:30
 * @PackageName:com.atjixue.gulimall.seckill.config.service
 * @ClassName: SecKillService
 * @Description: TODO
 * @Version 1.0
 */
public interface SecKillService {
    void uploadSeckillSkuLatest3Days();

    List<SecKillSkuRedisTo> getCurrentSeckillSkus();

    SecKillSkuRedisTo getSkuSecKillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
