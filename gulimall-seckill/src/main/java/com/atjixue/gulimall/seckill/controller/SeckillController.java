package com.atjixue.gulimall.seckill.controller;

import com.atjixue.common.utils.R;
import com.atjixue.gulimall.seckill.service.SecKillService;
import com.atjixue.gulimall.seckill.to.SecKillSkuRedisTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/26 17:02
 * @PackageName:com.atjixue.gulimall.seckill.controller
 * @ClassName: SeckillController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
public class SeckillController {
    @Autowired
    SecKillService secKillService;

    /**
     * 返回当前时间可以参与的秒杀商品信息
     */
    @GetMapping("/currentSeckillSkus")
    @ResponseBody
    public R getCurrentSeckillSkus() {
        List<SecKillSkuRedisTo> list = secKillService.getCurrentSeckillSkus();
        return R.ok().setData(list);
    }

    @GetMapping("/skuSeckillInfo/{skuId}")
    @ResponseBody
    public R getSkuSeckillInfo(@PathVariable("skuId") Long skuId) {
        SecKillSkuRedisTo to = secKillService.getSkuSecKillInfo(skuId);
        return R.ok().setData(to);
    }

    @GetMapping()
    public String seckill(@RequestParam("killId") String killId,
                     @RequestParam("key") String key,
                     @RequestParam("num") Integer num,
                          Model model) {
        //1、判断是否登陆
        String orderSn = secKillService.kill(killId, key, num);
        model.addAttribute("orderSn",orderSn);
        return "success";
    }
}
