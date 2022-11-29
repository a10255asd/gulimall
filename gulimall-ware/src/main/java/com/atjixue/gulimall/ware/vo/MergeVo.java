package com.atjixue.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/14 18:39
 * @PackageName:com.atjixue.gulimall.ware.vo
 * @ClassName: MergeVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
