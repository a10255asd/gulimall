package com.atjixue.gulimall.cart.interceptor;

import com.atjixue.common.constant.AuthServerConstant;
import com.atjixue.common.constant.CartConstant;
import com.atjixue.common.vo.MemberRespVo;
import com.atjixue.gulimall.cart.vo.UserInfoTo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @Author LiuJixue
 * @Date 2022/9/2 17:15
 * @PackageName:com.atjixue.gulimall.cart.interceptor
 * @ClassName: CartInterceptor
 * @Description: 在执行目标方法之前，判断用户的登录状态，并封装传递给controller目标请求
 * @Version 1.0
 */
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();
    /**
     * 目标方法执行之前
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        MemberRespVo member = (MemberRespVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if(member != null ){
            // 用户登录了
            userInfoTo.setUserId(member.getId());
        }else{
            // 用户没登录
        }

        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                // user-key
                String name = cookie.getName();
                if(name.equals(CartConstant.TEMP_USER_COOKIE_NAME)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }
        // 如果没有临时用户 一定分配一个临时用户
        if(StringUtils.isEmpty(userInfoTo.getUserKey())){
            String uuid =  UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }
        // 目标方法执行之前
        threadLocal.set(userInfoTo);
        return true;
    }
    /**
     * 分配临时用户 让浏览器保存
     * 业务执行之后
     * */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        // 如果没有临时用户，一定保存一个临时用户
        if(!userInfoTo.getTempUser()){
            // 持续的延长临时用户的过期时间
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
