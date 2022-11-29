package com.atjixue.gulimall.ware.service.impl;

import com.atjixue.gulimall.ware.entity.WareSkuEntity;
import com.qiniu.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.common.utils.Query;

import com.atjixue.gulimall.ware.dao.PurchaseDetailDao;
import com.atjixue.gulimall.ware.entity.PurchaseDetailEntity;
import com.atjixue.gulimall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {
    /**
     * status:0;状态
     * wareId 1 仓库id
     * */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isNullOrEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("purchase_id",key).or().eq("sku_id",key);
            });
        }

        String status = (String) params.get("status");;
        if(!StringUtils.isNullOrEmpty(status)){
            queryWrapper.eq("status",status);
        }
        String wareId = (String) params.get("wareId");;
        if(!StringUtils.isNullOrEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {
        List<PurchaseDetailEntity> purchaseId = this.list(new QueryWrapper<PurchaseDetailEntity>().eq("Purchase_id", id));
        return purchaseId;
    }

}