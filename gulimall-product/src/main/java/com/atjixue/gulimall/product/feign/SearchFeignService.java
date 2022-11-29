package com.atjixue.gulimall.product.feign;

import com.atjixue.common.to.es.SkuEsModel;
import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/17 22:27
 * @PackageName:com.atjixue.gulimall.product.feign
 * @ClassName: SearchFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModel);
}
