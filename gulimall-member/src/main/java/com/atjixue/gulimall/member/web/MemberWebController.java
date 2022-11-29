package com.atjixue.gulimall.member.web;

import com.alibaba.fastjson.JSON;
import com.atjixue.common.utils.R;
import com.atjixue.gulimall.member.feign.OrderFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author LiuJixue
 * @Date 2022/9/22 16:05
 * @PackageName:com.atjixue.gulimall.member.web
 * @ClassName: MemberWebController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
public class MemberWebController {
    @Autowired
    OrderFeignService orderFeignService;
    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                  Model model, HttpServletRequest request){
        //获取到支付宝给我们传来的所有请求数据  request 验证前面 如果正确可以修改
        // 查询当前登录用户的所有订单列表数据
        Map<String,Object> page = new HashMap<>();
        page.put("page",pageNum.toString());
        R r = orderFeignService.listWithItem(page);
        System.out.println(JSON.toJSONString(r));
        model.addAttribute("orders",r);
        return "orderList";
    }
}
