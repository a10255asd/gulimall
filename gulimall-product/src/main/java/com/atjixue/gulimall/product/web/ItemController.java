package com.atjixue.gulimall.product.web;

import com.atjixue.gulimall.product.service.SkuInfoService;
import com.atjixue.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @Author LiuJixue
 * @Date 2022/8/28 21:06
 * @PackageName:com.atjixue.gulimall.product.web
 * @ClassName: ItemController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;
    /**
     * 展示当前sku的详情
     * */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {

        System.out.println("准备查询" +skuId + "详情");
        SkuItemVo vo =  skuInfoService.item(skuId);
        model.addAttribute("item",vo);
        return "item";
    }
}
