package com.atjixue.gulimall.cart.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Author LiuJixue
 * @Date 2022/9/2 17:21
 * @PackageName:com.atjixue.gulimall.cart.vo
 * @ClassName: UserInfoVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
@ToString
public class UserInfoTo {
    private Long userId;
    // 一定有
    private String userKey;
    private Boolean tempUser = false;

}
