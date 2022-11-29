package com.atjixue.gulimall.order.vo;

import com.atjixue.gulimall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @Author LiuJixue
 * @Date 2022/9/8 15:11
 * @PackageName:com.atjixue.gulimall.order.vo
 * @ClassName: SubmitOrderResponseVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class SubmitOrderResponseVo {
    private OrderEntity order;
    private Integer code; // 错误状态码 0 成功

}
