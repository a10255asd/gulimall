package com.atjixue.gulimall.auth.feign;

import com.atjixue.common.utils.R;
import com.atjixue.gulimall.auth.vo.SocialUserVo;
import com.atjixue.gulimall.auth.vo.UserLoginVo;
import com.atjixue.gulimall.auth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author LiuJixue
 * @Date 2022/8/30 23:31
 * @PackageName:com.atjixue.gulimall.auth.feign
 * @ClassName: MemFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-member")
public interface MemFeignService {
    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth2/login")
    R oauthlogin(@RequestBody SocialUserVo socialUserVo) throws Exception;
}
