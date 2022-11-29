package com.atjixue.gulimall.auth.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author LiuJixue
 * @Date 2022/8/31 22:52
 * @PackageName:com.atjixue.gulimall.auth.vo
 * @ClassName: SocialUserVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class SocialUserVo implements Serializable {

    private String access_token;

    private String token_type;

    private int expires_in;

    private String refresh_token;

    private String scope;

    private Integer created_at;

    private Integer socialUid;


}
