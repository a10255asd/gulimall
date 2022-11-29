package com.atjixue.gulimall.product.exception;

import com.atjixue.common.utils.R;
import com.atjixue.common.exception.BizCodeEnume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author LiuJixue
 * @Date 2022/8/8 22:24
 * @PackageName:com.atjixue.gulimall.product.exception
 * @ClassName: GulimallExceptionControllerAdvice
 * @Description: 集中处理所有异常
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.atjixue.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e){
        log.error("数据校验出现问题{},异常类型：{}",e.getMessage(),e.getClass() );
        BindingResult bindingResult  = e.getBindingResult();
        Map<String,String> errMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError)->{
            errMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",errMap);
    }


    @ExceptionHandler(value = Throwable.class)
    public  R handleException(Throwable e){
        log.error("异常类型：{}",e.getMessage(),e.getClass() );
        e.printStackTrace();
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
