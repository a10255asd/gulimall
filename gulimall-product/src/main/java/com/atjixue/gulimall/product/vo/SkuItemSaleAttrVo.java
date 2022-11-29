package com.atjixue.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/28 22:57
 * @PackageName:com.atjixue.gulimall.product.vo
 * @ClassName: SkuItemSaleAttrVo
 * @Description: TODO
 * @Version 1.0
 */
@ToString
@Data
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;
}
