package com.atjixue.gulimall.product.feign;

import com.atjixue.common.utils.R;
import com.atjixue.gulimall.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/17 16:24
 * @PackageName:com.atjixue.gulimall.product.feign
 * @ClassName: WareFeignService
 * @Description: TODO
 * @Version 1.0
 */
/**
 * 1、R 在设计的时候可以加上范型
 * 2、直接返回想要的结果
 * 3、自己封装解析结果
 * */
@FeignClient("gulimall-ware")
public interface WareFeignService {
    //ware/waresku
    @PostMapping("/ware/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuids);
}
