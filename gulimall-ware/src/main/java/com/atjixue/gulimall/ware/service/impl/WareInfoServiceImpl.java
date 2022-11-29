package com.atjixue.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.atjixue.common.utils.R;
import com.atjixue.gulimall.ware.feign.MemeberFeignService;
import com.atjixue.gulimall.ware.vo.FareVo;
import com.atjixue.gulimall.ware.vo.MemberAddressVo;
import com.qiniu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.common.utils.Query;

import com.atjixue.gulimall.ware.dao.WareInfoDao;
import com.atjixue.gulimall.ware.entity.WareInfoEntity;
import com.atjixue.gulimall.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {
    @Autowired
    MemeberFeignService memeberFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(key)){
            wrapper.eq("id",key)
                    .or().like("name",key)
                    .or().like("address",key)
                    .or().like("areacode",key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public FareVo getFare(Long addrId) {
        R r = memeberFeignService.addrInfo(addrId);
        FareVo fareVo = new FareVo();
        MemberAddressVo data = r.getData("memberReceiveAddress",new TypeReference<MemberAddressVo>() {
        });
        if(data != null){
            String phone = data.getPhone();
            // 123456789
            String substring = phone.substring(phone.length() - 1, phone.length());
            BigDecimal bigDecimal = new BigDecimal(substring);
            fareVo.setAddress(data);
            fareVo.setFare(bigDecimal);
            return fareVo;
        }
        return null;

    }

}