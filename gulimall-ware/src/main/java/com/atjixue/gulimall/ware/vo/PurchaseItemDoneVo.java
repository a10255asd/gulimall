package com.atjixue.gulimall.ware.vo;

import lombok.Data;

/**
 * @Author LiuJixue
 * @Date 2022/8/14 20:24
 * @PackageName:com.atjixue.gulimall.ware.vo
 * @ClassName: ItemVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class PurchaseItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
