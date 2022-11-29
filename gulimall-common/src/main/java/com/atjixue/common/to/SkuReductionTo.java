package com.atjixue.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/13 17:50
 * @PackageName:com.atjixue.common.to
 * @ClassName: SkuReduction
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;

    private List<MemberPrice> memberPrice;
}
