package com.atjixue.gulimall.product.vo;

import com.atjixue.gulimall.product.entity.SkuImagesEntity;
import com.atjixue.gulimall.product.entity.SkuInfoEntity;
import com.atjixue.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/28 21:21
 * @PackageName:com.atjixue.gulimall.search.vo
 * @ClassName: SkuItemVo
 * @Description: TODO
 * @Version 1.0
 */
@ToString
@Data
public class SkuItemVo {
    // 1、sku基本信息获取 pms_sku_info
    SkuInfoEntity info;

    boolean hasStock = true;
    // 2、sku的图片信息 pms_sku_imges
    List<SkuImagesEntity> images;
     // 3、获取spu的销售属性组合
    List<SkuItemSaleAttrVo> saleAttr;
    // 4、获取spu的介绍
    SpuInfoDescEntity desp;
    // 5、获取spu的规格参数信息
    List<SpuItemAttrGroupVo> groupAttrs;
    //当前商品的秒杀优惠信息
    private SecKillnfoVo secKillnfo;

}
