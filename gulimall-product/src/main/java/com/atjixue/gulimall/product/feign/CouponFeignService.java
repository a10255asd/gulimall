package com.atjixue.gulimall.product.feign;

import com.atjixue.common.to.SkuReductionTo;
import com.atjixue.common.to.SpuBoundsTo;
import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author LiuJixue
 * @Date 2022/8/13 16:46
 * @PackageName:com.atjixue.gulimall.product.feign
 * @ClassName: CouponfeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
