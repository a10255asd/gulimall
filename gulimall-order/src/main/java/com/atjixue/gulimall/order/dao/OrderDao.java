package com.atjixue.gulimall.order.dao;

import com.atjixue.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 15:59:09
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
