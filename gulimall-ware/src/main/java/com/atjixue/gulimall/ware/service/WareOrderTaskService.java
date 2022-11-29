package com.atjixue.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.gulimall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 16:06:30
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn);
}

