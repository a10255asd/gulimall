package com.atjixue.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/24 10:23
 * @PackageName:com.atjixue.gulimall.search.vo
 * @ClassName: SearchParam
 * @Description: 封住页面所有可能传递过来的查询条件
 * @Version 1.0
 */
@Data
public class SearchParam {

    // 页面传递过来的全文匹配关键字
    private String keyword;
    //三级分类Id
    private Long catalog3Id;
    /**
     * 排序条件
     * sort= saleCount_asc/desc
     * sort= skuPrice_asc/desc
     * sort= hotSore_asc/desc
     * */
    private String sort;
    /**
     * 好多的过滤条件
     * hasStock 0/1 是否有货
     * skuPrice = 1_500/_500/500_价格区间
     * brandId = 1
     * attrs=2_5寸：6寸
     *
     * */
    // 是否只显示有货
    private Integer hasStock;
    // 价格区间
    private String skuPrice;
    // 品牌Id,可以多选
    private List<Long> brandId;
    // 按照属性进行筛选
    private List<String> attrs;
    // 页码
    private Integer pageNum =1;

    private String _queryString; //原生的所有查询条件

}
