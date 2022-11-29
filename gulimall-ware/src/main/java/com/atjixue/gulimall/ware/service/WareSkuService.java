package com.atjixue.gulimall.ware.service;

import com.atjixue.common.to.mq.OrderTo;
import com.atjixue.common.to.mq.StockLockedTo;
import com.atjixue.gulimall.ware.vo.LockStockResult;
import com.atjixue.gulimall.ware.vo.SkuHasStockVo;
import com.atjixue.gulimall.ware.vo.WareSkuLockVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 16:06:30
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuids);

    Boolean orderLockStock(WareSkuLockVo vo);


    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo to);
}

