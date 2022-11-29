package com.atjixue.gulimall.order.interceptor;

import com.atjixue.common.constant.AuthServerConstant;
import com.atjixue.common.vo.MemberRespVo;
import org.apache.shiro.util.AntPathMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author LiuJixue
 * @Date 2022/9/6 16:35
 * @PackageName:com.atjixue.gulimall.order.interceptor
 * @ClassName: LoginUserInterceptor
 * @Description: TODO
 * @Version 1.0
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 请求路径是 order/order/status/{orderSn}
        String uri = request.getRequestURI();
        boolean match = new AntPathMatcher().match("/order/order/status/**", uri);
        if(match){
            return true;
        }
        MemberRespVo attribute = (MemberRespVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
        if(attribute != null){
            loginUser.set(attribute);
            return true;
        }else {
            // 没登录 就去登录
            request.getSession().setAttribute("msg","请先进行登录");
            response.sendRedirect("http://auth.gulimall.com/login.html");
            return false;
        }

    }
}
