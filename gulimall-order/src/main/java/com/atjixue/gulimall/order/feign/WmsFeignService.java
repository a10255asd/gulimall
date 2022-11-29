package com.atjixue.gulimall.order.feign;

import com.atjixue.common.utils.R;
import com.atjixue.gulimall.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/7 14:35
 * @PackageName:com.atjixue.gulimall.order.feign
 * @ClassName: WmsFeign
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-ware")
public interface WmsFeignService {
    @PostMapping("/ware/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuids);

    @GetMapping("/ware/wareinfo/fare")
    R getFare(@RequestParam("addrId") Long addrId);

    @PostMapping("/ware/waresku/lock/order")
    R orderLockStock(@RequestBody WareSkuLockVo vo);
}
