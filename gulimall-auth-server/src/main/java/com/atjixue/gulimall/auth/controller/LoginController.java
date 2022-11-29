package com.atjixue.gulimall.auth.controller;

import com.atjixue.common.constant.AuthServerConstant;
import com.atjixue.common.exception.BizCodeEnume;
import com.atjixue.common.utils.R;
import com.atjixue.common.vo.MemberRespVo;
import com.atjixue.gulimall.auth.feign.MemFeignService;
import com.atjixue.gulimall.auth.feign.ThirdPartFeignService;
import com.atjixue.gulimall.auth.vo.UserLoginVo;
import com.atjixue.gulimall.auth.vo.UserRegistVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.fastjson.TypeReference;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author LiuJixue
 * @Date 2022/8/29 16:48
 * @PackageName:com.atjixue.gulimall.auth.controller
 * @ClassName: LoginController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
public class LoginController {

    @Autowired
    ThirdPartFeignService thirdPartFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    MemFeignService memFeignService;

    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone){
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if(!StringUtils.isEmpty(redisCode)){
            long l = Long.parseLong(redisCode.split("_")[1]);
            if(System.currentTimeMillis() - l < 60000){
                // 60秒内不能再发
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            };
        }
        // TODO 1、接口防刷
        // 2、验证码的再次校验 sms:code:17685733719 -->667788
        String code = UUID.randomUUID().toString().substring(0, 5) + "_" + System.currentTimeMillis();
        // Redis 缓存验证码 ，防止同一个手机号 在60s内再次放松验证码
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone,code,10, TimeUnit.MINUTES);
        thirdPartFeignService.sendCode(phone,code.substring(0, 5));
        return R.ok();
    }

    /**
     * TODO 重定向携带数据，利用session原理，分布式模式下使用session 会出现一些问题
     * RedirectAttributes redirectAttributes: 模拟重定向携带数据 是模拟session
     * .map((fieldError)->{
     *                 String field = fieldError.getField();
     *                 String defaultMessage = fieldError.getDefaultMessage();
     *                 errors.put(field,defaultMessage);
     *                 return errors
     *             })
     *  TODO 1、分布式下的session 问题
     * */
    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result,
                         RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField
            ,FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors",errors);
            //Request method 'POST' not supported
            // 用户注册-》/regist[post] ->转发- /reg.html 路径映射默认都是get方式访问的

            // 如果校验出错，转发到注册页面
            return "redirect:http://auth.gulimall.com/reg.html";
        }
        // 1、校验验证码
        String code = vo.getCode();

        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if(!StringUtils.isEmpty(s)){
            if(code.equalsIgnoreCase(s.split("_")[0])){
                // 删除验证码,令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                //验证码通过 真正的注册，调用远程服务
                R r = memFeignService.regist(vo);
                if(r.getCode() == 0){
                    // 成功
                    return "redirect:http://auth.gulimall.com//login.html";
                }else {
                    Map<String,String> errors = new HashMap<>();
                    errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute("erros",errors);
                    return "redirect:http://auth.gulimall.com/reg.html";
                }

            }else{
                Map<String, String> errors = new HashMap<>();
                errors.put("code","验证码错误");
                redirectAttributes.addFlashAttribute("errors",errors);
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        }else{
            Map<String, String> errors = new HashMap<>();
            errors.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/reg.html";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes,
            HttpSession session){
        R r = memFeignService.login(vo);
        if(r.getCode() == 0){
            // 远程登录，
            MemberRespVo data = r.getData("data", new TypeReference<MemberRespVo>() {
            });
            session.setAttribute(AuthServerConstant.LOGIN_USER,data);
            return "redirect:http://gulimall.com";
        }else {
            Map<String,String> errors = new HashMap<>();
            errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session){
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if(attribute == null){
            // 没登录展示登录页
            return "login";
        }else {
            return "redirect:http://gulimall.com";
        }

    }
}
