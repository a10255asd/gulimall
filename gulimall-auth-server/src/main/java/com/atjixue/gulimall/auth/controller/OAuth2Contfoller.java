package com.atjixue.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.atjixue.common.utils.HttpUtils;
import com.atjixue.common.utils.R;
import com.atjixue.common.vo.MemberRespVo;
import com.atjixue.gulimall.auth.feign.MemFeignService;

import com.atjixue.gulimall.auth.vo.SocialUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author LiuJixue
 * @Date 2022/8/31 22:14
 * @PackageName:com.atjixue.gulimall.auth.controller
 * @ClassName: OAuth2Contfoller
 * @Description: 处理社交登录请求
 * @Version 1.0
 */
@Controller
@Slf4j
public class OAuth2Contfoller {
    @Autowired
    MemFeignService memFeignService;

    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code , HttpSession session , HttpServletResponse servletResponse) throws Exception {
        //1.根据code 获取accsee_token
        Map<String, String> map = new HashMap<>();
        map.put("client_id","d4fa7bf28adaee5e038e0d783052520ada414037f753627e6e51b432ed9e8234");
        map.put("redirect_uri","http://auth.gulimall.com/oauth2.0/weibo/success");
        map.put("client_secret","24f8a6e1fb3a9d31e6518348f4843f2e7b3733313e3b1efda5946d8db08b0fcb");
        map.put("grant_type","authorization_code");
        map.put("code",code);
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", new HashMap<>(), new HashMap<>(),map);
        //2.登录成功就跳回首页
        if(response.getStatusLine().getStatusCode() == 200){
            // 获取到accesstoken
            String json = EntityUtils.toString(response.getEntity());
            SocialUserVo socialUserVo = JSON.parseObject(json, SocialUserVo.class);
            // 知道当前是哪个社交用户
            // 1、当前用户如果是第一次进网站，自动注册（为当前社交用户生产一个会员信息账号），以后整个社交账号
            //就对应指定的会员
            //登录或者注册这个社交用户
            Map<String, String> mapUser = new HashMap<>();
            mapUser.put("access_token",socialUserVo.getAccess_token());
            HttpResponse getUid = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", new HashMap<>(),mapUser);
            if(getUid.getStatusLine().getStatusCode() == 200) {
                String s = EntityUtils.toString(getUid.getEntity());
                JSONObject jsonObject = JSON.parseObject(s);
                String id = jsonObject.getString("id");
                socialUserVo.setSocialUid(Integer.parseInt(id));
                R oauthlogin = memFeignService.oauthlogin(socialUserVo);
                if(oauthlogin.getCode() == 0){
                    MemberRespVo data = oauthlogin.getData("data", new TypeReference<MemberRespVo>() {
                    });
                    log.info("登录成功：用户信息是：{}",data.toString());
                    // 第一次使用session，命令浏览器保存卡号，jsessionid这个cookie
                    // 以后浏览器访问哪个网站就会带上这个网站的cookie
                    // 子域之间 gulimall.com auth.gulimall.com order.gulimall.com
                    //  在发卡的时候即使是子域系统发的卡，发卡的时候指定域名，指定为父域名，也能让父域直接使用
                    // TODO 默认发的令牌 session =  sss 作用域是当前域。解决子域session共享
                    // TODO 使用JSON的序列化方式序列化对象数据到redis
                    session.setAttribute("loginUser",data);
//                    new Cookie("JSESSIONID","dada").setDomain();
//                    servletResponse.addCookie();
                    return "redirect:http://gulimall.com";
                }else{
                    return "redirect:http://auth.gulimall.com/login.html";
                }
            }
        }else {
            // 不成功
            return "redirect:http://auth.gulimall.com/login.html";
        }
        return "redirect:http://gulimall.com";
    }
}
