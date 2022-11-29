package com.atjixue.gulimall.product.service;

import com.atjixue.gulimall.product.vo.AttrGroupRelationVo;
import com.atjixue.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.atjixue.gulimall.product.vo.AttrRespVo;
import com.atjixue.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.gulimall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 12:43:18
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, long attrgroupId);

    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

