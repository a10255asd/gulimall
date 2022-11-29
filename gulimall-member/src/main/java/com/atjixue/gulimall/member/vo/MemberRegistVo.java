package com.atjixue.gulimall.member.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @Author LiuJixue
 * @Date 2022/8/30 22:24
 * @PackageName:com.atjixue.gulimall.member.vo
 * @ClassName: MemberRegistVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class MemberRegistVo {

    private String userName;
    private String password;
    private String phone;

}
