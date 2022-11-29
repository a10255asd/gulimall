package com.atjixue.gulimall.product.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author LiuJixue
 * @Date 2022/9/27 00:01
 * @PackageName:com.atjixue.gulimall.product.feign
 * @ClassName: SeckillFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-seckill")
public interface SeckillFeignService {
    @GetMapping("/skuSeckillInfo/{skuId}")
    R getSkuSeckillInfo(@PathVariable("skuId") Long skuId);
}
