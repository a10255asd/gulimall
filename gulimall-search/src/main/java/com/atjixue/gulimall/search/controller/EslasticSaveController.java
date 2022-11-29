package com.atjixue.gulimall.search.controller;

import com.atjixue.common.exception.BizCodeEnume;
import com.atjixue.common.to.es.SkuEsModel;
import com.atjixue.common.utils.R;
import com.atjixue.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/17 16:51
 * @PackageName:com.atjixue.gulimall.search.controller
 * @ClassName: EslasticSave
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/search/save")
public class EslasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModel) {
        Boolean b = false;
        try{
            b = productSaveService.productStatusUp(skuEsModel);

        }catch (Exception e){
            log.error("EslasticSaveController商品上架错误：{}",e);
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg() );
        }
        if(!b){
            return R.ok();
        }else{
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg() );
        }
    }
}
