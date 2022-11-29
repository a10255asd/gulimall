package com.atjixue.gulimall.product.service;

import com.atjixue.gulimall.product.vo.SkuItemSaleAttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.gulimall.product.entity.SkuSaleAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 12:43:19
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId);

    List<String> getSkuSaleValuesAsStringList(Long skuId);
}

