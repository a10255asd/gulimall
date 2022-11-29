package com.atjixue.gulimall.search.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/27 20:31
 * @PackageName:com.atjixue.gulimall.search.feign
 * @ClassName: ProductFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("/product/attr/info/{attrId}")
    public R attrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/attr//infos")
    public R bradnInfo(@RequestParam("brandId") List<Long> brandIds);
}
