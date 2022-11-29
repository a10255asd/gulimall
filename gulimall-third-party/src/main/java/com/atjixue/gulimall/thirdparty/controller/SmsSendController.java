package com.atjixue.gulimall.thirdparty.controller;

import com.atjixue.common.utils.R;
import com.atjixue.gulimall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuJixue
 * @Date 2022/8/29 23:36
 * @PackageName:com.atjixue.gulimall.thirdparty.controller
 * @ClassName: SmsSendController
 * @Description: TODO
 * @Version 1.0
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @Autowired
    SmsComponent smsComponent;

    // 提供给别的服务进行调用
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code){
        System.out.println("-------------验证码是：" + code);
        smsComponent.sendSmsCode(phone,code);
        return R.ok();
    }
}
