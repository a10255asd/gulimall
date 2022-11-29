package com.atjixue.gulimall.order.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author LiuJixue
 * @Date 2022/9/10 01:56
 * @PackageName:com.atjixue.gulimall.order.feign
 * @ClassName: ProductFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    @GetMapping("/product/spuinfo/skuId/{id}")
    R getSpuInfoBySkuId(@PathVariable("id") Long skuId);
}
