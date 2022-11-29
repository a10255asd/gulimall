package com.atjixue.common.to.mq;

import com.atjixue.common.to.StockDetailTo;
import lombok.Data;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/17 23:22
 * @PackageName:com.atjixue.common.to.mq
 * @ClassName: StockLockedTo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class StockLockedTo {
    // 库存工作单的id
    private Long id;
    // 工作单详情的所有id
    private StockDetailTo detail;
}
