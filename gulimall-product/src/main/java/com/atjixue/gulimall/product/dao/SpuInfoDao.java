package com.atjixue.gulimall.product.dao;

import com.atjixue.gulimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 12:43:19
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updateSpuStatus(@Param("spuId") Long spuId,@Param("code") int code);
}
