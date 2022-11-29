package com.atjixue.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/19 13:53
 * @PackageName:com.atjixue.gulimall.product.vo
 * @ClassName: Catelog2Vo
 * @Description: TODO
 * @Version 1.0
 */
// 2级分类vo
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {
    private String catalog1Id; //1 级父分类id
    private List<catelog3Vo> catalog3List; //三级子分类
    private String id;
    private String name;

    // 三级分类vo
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class catelog3Vo {
        private String catalog2Id; //父分类id
        private String id;
        private String name;

    }

}
