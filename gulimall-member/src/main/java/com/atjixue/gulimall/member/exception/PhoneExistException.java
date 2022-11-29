package com.atjixue.gulimall.member.exception;

/**
 * @Author LiuJixue
 * @Date 2022/8/30 22:44
 * @PackageName:com.atjixue.gulimall.member.exception
 * @ClassName: PhoneExistException
 * @Description: TODO
 * @Version 1.0
 */
public class PhoneExistException extends RuntimeException{
    public PhoneExistException() {
        super("手机号存在异常");
    }
}
