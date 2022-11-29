package com.atjixue.gulimall.member.feign;

import com.atjixue.common.to.SpuBoundsTo;
import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @Author LiuJixue
 * @Date 2022/8/1 22:29
 * @PackageName:com.atjixue.gulimall.member.feign
 * @ClassName: CouponFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {


}
