package com.atjixue.gulimall.order.web;

import com.alipay.api.AlipayApiException;
import com.atjixue.gulimall.order.config.AlipayTemplate;
import com.atjixue.gulimall.order.service.OrderService;
import com.atjixue.gulimall.order.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author LiuJixue
 * @Date 2022/9/20 17:07
 * @PackageName:com.atjixue.gulimall.order.web
 * @ClassName: PayWebController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
public class PayWebController {

    @Autowired
    AlipayTemplate alipayTemplate;
    @Autowired
    OrderService orderService;

    @ResponseBody
    @GetMapping(value = "/payOrder",produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
//        PayVo payVo = new PayVo();
//        payVo.setBody(); //订单备注
//        payVo.setOut_trade_no(); // 订单号
//        payVo.setSubject(); // 标题
//        payVo.setTotal_amount(); // 金额
        PayVo payVo =  orderService.getOrderPay(orderSn);
        // 返回的是一个页面，将此页面直接交给浏览器就行
        String pay = alipayTemplate.pay(payVo);
        System.out.println(pay);
        return pay;
    }
}
