package com.atjixue.gulimall.product.service.impl;

import com.atjixue.gulimall.product.entity.AttrEntity;
import com.atjixue.gulimall.product.service.AttrService;
import com.atjixue.gulimall.product.vo.Attr;
import com.atjixue.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.atjixue.gulimall.product.vo.SkuItemVo;
import com.atjixue.gulimall.product.vo.SpuItemAttrGroupVo;
import com.qiniu.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.common.utils.Query;

import com.atjixue.gulimall.product.dao.AttrGroupDao;
import com.atjixue.gulimall.product.entity.AttrGroupEntity;
import com.atjixue.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    AttrService attrService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper =  new QueryWrapper<AttrGroupEntity>();
        if(!StringUtils.isNullOrEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id",catelogId).or().like("attr_group_name",key);
            });
        }
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params)
                    , wrapper);
            return new PageUtils(page);
        }else{
            // select * from pms_attr_group where catelog_id = ? and (attr_group_id = key or attr_group_name like %key%)
            wrapper.eq("catelog_id",catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params)
                    , wrapper);
            return new PageUtils(page);
        }
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //1、查询分组信息
        //2、查询所有属性
        List<AttrGroupEntity> list = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId));
        List<AttrGroupWithAttrsVo> collect = list.stream().map((group) -> {
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group,attrsVo);
            List<AttrEntity> attrs = attrService.getRelationAttr(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(attrs);
            return attrsVo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override

    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        // 查出当前spu对应的所有属性的分组信息，以及当前分组下所有属性对应的值
        //
        AttrGroupDao baseMapper = this.getBaseMapper();
        List<SpuItemAttrGroupVo> vos =  baseMapper.getAttrGroupWithAttrsBySpuId(spuId,catalogId);
        return vos;
    }

}