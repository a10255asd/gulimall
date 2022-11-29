package com.atjixue.gulimall.cart.controller;

import com.atjixue.gulimall.cart.service.CartService;
import com.atjixue.gulimall.cart.vo.Cart;
import com.atjixue.gulimall.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author LiuJixue
 * @Date 2022/9/2 17:01
 * @PackageName:com.atjixue.gulimall.cart.controller
 * @ClassName: CartController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
public class CartController {
    /**
     * 浏览器有一个cookie：user-key 标识用户身份，一个月后过期
     * 如果第一次使用京东的购物车功能，都会给一个临时的用户身份
     * 浏览器每次访问都会带上这个cookie
     *
     * 登录：session里面有
     * 没登录：按照cookie里面的user-key来做
     *
     * 第一次如果没有临时用户，还要帮忙创建一个临时用户
     * */
    @Autowired
    CartService cartService;

    @GetMapping("/currentUserCartItems")
    @ResponseBody
    public List<CartItem> getCurrentUserCartItems(){
        return cartService.getUserCartItems();
    }

    @GetMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        // 快速得到用户信息 id user-key
        Cart cart = cartService.getCart();
        model.addAttribute("cart",cart);
        return "cartList";
    }
    /**
     * 添加商品到购物车
     * RedirectAttributes 将数据放到session里面 可以在页面取出，但是只能取一次
     * ra.addAttribute 将数据放到
     * */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                            RedirectAttributes ra) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId,num);
        ra.addAttribute("skuId",skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }
    /**
     * 跳转到成功页面
     * */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,Model model){
        //重定向到成功页面，再次查询购物车数据
        CartItem item = cartService.getCartItem(skuId);
        model.addAttribute("item",item);
        return "success";
    }
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("checked") Integer checked){
        cartService.checkItem(skuId,checked);
        return "redirect:http://cart.gulimall.com/cart.html";
    }
    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num){
        cartService.changeItemCount(skuId,num);
        return "redirect:http://cart.gulimall.com/cart.html";
    }
    @GetMapping("deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId){
        cartService.deleteItem(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }
}
