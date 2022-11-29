package com.atjixue.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/28 22:52
 * @PackageName:com.atjixue.gulimall.product.vo
 * @ClassName: SpuItemAttrGroupVo
 * @Description: TODO
 * @Version 1.0
 */
@ToString
@Data
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<Attr> attrs;
}
