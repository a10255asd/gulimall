package com.atjixue.gulimall.product.dao;

import com.atjixue.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.atjixue.gulimall.product.vo.SkuItemSaleAttrVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 12:43:19
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(@Param("spuId") Long spuId);

    List<String> getSkuSaleValuesAsStringList(@Param("skuId") Long skuId);
}
