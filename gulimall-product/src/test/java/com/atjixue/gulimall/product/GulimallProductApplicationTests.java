package com.atjixue.gulimall.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.atjixue.gulimall.product.dao.AttrGroupDao;
import com.atjixue.gulimall.product.dao.SkuSaleAttrValueDao;
import com.atjixue.gulimall.product.entity.BrandEntity;
import com.atjixue.gulimall.product.service.BrandService;
import com.atjixue.gulimall.product.service.CategoryService;
import com.atjixue.gulimall.product.vo.SkuItemSaleAttrVo;
import com.atjixue.gulimall.product.vo.SkuItemVo;
import com.atjixue.gulimall.product.vo.SpuItemAttrGroupVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
*
 * 1、引入oss-starter
 * 2、配置key、endpoint相关信息即可
 * 3、使用 OSSClient 进行相关操作
* */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GulimallProductApplicationTests {

//    @Autowired
//    OSS ossClient;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;
    @Test
    public void test1(){
//        List<SpuItemAttrGroupVo> group = attrGroupDao.getAttrGroupWithAttrsBySpuId(100L, 225L);
//        System.out.println(group);
        List<SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(13L);
        System.out.println(saleAttrsBySpuId);
    }
    @Test
    public void redis(){
        System.out.println(redissonClient);
    }
    @Test
    public void test(){
        // hello world
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // 保存
        ops.set("hello","world" + UUID.randomUUID().toString());
        //查询
        String hello = ops.get("hello");
        System.out.println("之前保存的数据是" + hello);
    }
    @Test
    public void testFindPath(){

        Long[] catelogPath =  categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }
    @Test
    public void contextLoads() {

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("华为");
//        brandEntity.setDescript("");
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");
        //brandService.updateById(brandEntity);
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(item -> {
            System.out.println(item);
        });
    }

}
