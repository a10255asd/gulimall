package com.atjixue.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.gulimall.coupon.entity.SeckillSessionEntity;

import java.util.List;
import java.util.Map;

/**
 * 秒杀活动场次
 *
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 15:04:05
 */
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SeckillSessionEntity> getLatest3DaySession();
}

