package com.atjixue.common.to;

import lombok.Data;

/**
 * @Author LiuJixue
 * @Date 2022/9/17 23:29
 * @PackageName:com.atjixue.common.to
 * @ClassName: StockDetailTo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class StockDetailTo {
    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private Long taskId;

    private Long wareId;

    private Integer lockStatus;
}
