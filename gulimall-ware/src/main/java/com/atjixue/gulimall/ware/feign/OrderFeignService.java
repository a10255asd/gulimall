package com.atjixue.gulimall.ware.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author LiuJixue
 * @Date 2022/9/18 18:22
 * @PackageName:com.atjixue.gulimall.ware.feign
 * @ClassName: OrderFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-order")
public interface OrderFeignService {
    @GetMapping("/order/order/status/{orderSn}")
    R getOrderStatus(@PathVariable("orderSn") String orderSn);
}
