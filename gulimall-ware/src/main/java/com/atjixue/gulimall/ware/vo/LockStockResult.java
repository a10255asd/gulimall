package com.atjixue.gulimall.ware.vo;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @Author LiuJixue
 * @Date 2022/9/10 17:29
 * @PackageName:com.atjixue.gulimall.ware.vo
 * @ClassName: LockStockResult
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class LockStockResult {

    private Long skuId;
    private Integer num;
    private Boolean locked;

}
