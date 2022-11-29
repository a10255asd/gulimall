package com.atjixue.gulimall.order.web;

import com.atjixue.gulimall.order.service.OrderService;
import com.atjixue.gulimall.order.vo.OrderConfirmVo;
import com.atjixue.gulimall.order.vo.OrderSubmitVo;
import com.atjixue.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

/**
 * @Author LiuJixue
 * @Date 2022/9/6 16:28
 * @PackageName:com.atjixue.gulimall.order.web
 * @ClassName: OrderWebController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
public class OrderWebController {
    @Autowired
    OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();

        model.addAttribute("orderConfirmData",confirmVo);
        //展示订单确认的数据
        return "confirm";
    }
    @PostMapping ("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes redirectAttributes){
        SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
        // 创建订单，验价格 验令牌 锁库存
        // 下单成功 来到支付选择页面
        // 下单失败回到订单确认页重新确认订单信息
        System.out.println("订单提交的数据。。。。。" + vo);
        if(responseVo.getCode() == 0){
            // 成功
            model.addAttribute("submitOrderResp",responseVo);
            return "pay";
        }else {
            String msg = "下单失败";
            switch (responseVo.getCode())  {
                case 1:msg+="订单信息过期,请刷新提交";break;
                case 2:msg+="订单商品发生变化,请确认后提交";break;
                case 3:msg+="库存锁定失败，商品库存不足";break;
            }
            redirectAttributes.addFlashAttribute("msg",msg);
            return "redirect:http://order.gulimall.com/toTrade";
        }
    }
}
