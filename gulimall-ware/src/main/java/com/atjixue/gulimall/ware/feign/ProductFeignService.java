package com.atjixue.gulimall.ware.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author LiuJixue
 * @Date 2022/8/14 21:10
 * @PackageName:com.atjixue.gulimall.ware.feign
 * @ClassName: ProductFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    /**
     * /product/skuinfo/info/{skuId}
     * 和
     * /api/product/skuinfo/info/{skuId}
     * 可以有两种写法
     * 1）、让所有请求 都过网关，给网关所在的机器发请求 配套
     *          gulimall-gateway /api/product/skuinfo/info/{skuId}
     * 2）、 直接让后台指定服务处理
     *      gulimall-product  /product/skuinfo/info/{skuId}
     * */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
