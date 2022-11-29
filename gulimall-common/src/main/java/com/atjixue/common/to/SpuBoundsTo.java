package com.atjixue.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author LiuJixue
 * @Date 2022/8/13 17:02
 * @PackageName:com.atjixue.common.to
 * @ClassName: SpuBoundsTo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class SpuBoundsTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
