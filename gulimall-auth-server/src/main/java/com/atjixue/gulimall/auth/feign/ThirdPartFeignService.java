package com.atjixue.gulimall.auth.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author LiuJixue
 * @Date 2022/8/29 23:53
 * @PackageName:com.atjixue.gulimall.auth.feign
 * @ClassName: ThirdPartFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-third-party")
public interface ThirdPartFeignService {
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
