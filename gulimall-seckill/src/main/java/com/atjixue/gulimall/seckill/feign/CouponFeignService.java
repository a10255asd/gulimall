package com.atjixue.gulimall.seckill.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author LiuJixue
 * @Date 2022/9/25 21:33
 * @PackageName:com.atjixue.gulimall.seckill.feign
 * @ClassName: CouponFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @GetMapping("/coupon/seckillsession/latest3DaySession")
    R getLatest3DaySession();
}
