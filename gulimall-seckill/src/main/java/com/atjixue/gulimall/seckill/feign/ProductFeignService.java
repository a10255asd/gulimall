package com.atjixue.gulimall.seckill.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author LiuJixue
 * @Date 2022/9/25 23:53
 * @PackageName:com.atjixue.gulimall.seckill.feign
 * @ClassName: ProductFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    @RequestMapping("/product/skuinfo/info/{id}")
    R getSkuInfo(@PathVariable("id") Long id);
}
