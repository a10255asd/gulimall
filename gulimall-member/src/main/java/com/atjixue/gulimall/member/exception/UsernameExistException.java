package com.atjixue.gulimall.member.exception;

/**
 * @Author LiuJixue
 * @Date 2022/8/30 22:43
 * @PackageName:com.atjixue.gulimall.member.exception
 * @ClassName: UsernameExistException
 * @Description: TODO
 * @Version 1.0
 */
public class UsernameExistException extends RuntimeException{

    public UsernameExistException() {
        super("用户名存在异常");
    }
}
