package com.atjixue.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/14 20:23
 * @PackageName:com.atjixue.gulimall.ware.vo
 * @ClassName: PurchaseDoneVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id; // 采购单id

    private List<PurchaseItemDoneVo> items;

}
