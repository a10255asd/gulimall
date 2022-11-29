package com.atjixue.gulimall.search.service;

import com.atjixue.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/17 16:54
 * @PackageName:com.atjixue.gulimall.search.service
 * @ClassName: ProductSaveService
 * @Description: TODO
 * @Version 1.0
 */

public interface ProductSaveService {
    Boolean productStatusUp(List<SkuEsModel> skuEsModel) throws IOException;
}
