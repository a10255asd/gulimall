package com.atjixue.gulimall.member.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author LiuJixue
 * @Date 2022/9/23 10:33
 * @PackageName:com.atjixue.gulimall.member.feign
 * @ClassName: OrderFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-order")
public interface OrderFeignService {

    @PostMapping("/order/order/listWithItem")
    R listWithItem(@RequestBody Map<String, Object> params);
}
