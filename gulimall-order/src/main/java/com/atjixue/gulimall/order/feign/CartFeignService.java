package com.atjixue.gulimall.order.feign;

import com.atjixue.gulimall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/6 22:37
 * @PackageName:com.atjixue.gulimall.order.feign
 * @ClassName: CartFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-cart")
public interface CartFeignService {
    @GetMapping("/currentUserCartItems")
    @ResponseBody
    List<OrderItemVo> getCurrentUserCartItems();
}
