package com.atjixue.gulimall.product.vo;

import lombok.Data;

/**
 * @Author LiuJixue
 * @Date 2022/8/10 21:57
 * @PackageName:com.atjixue.gulimall.product.vo
 * @ClassName: attrRespVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class AttrRespVo extends AttrVo{
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
