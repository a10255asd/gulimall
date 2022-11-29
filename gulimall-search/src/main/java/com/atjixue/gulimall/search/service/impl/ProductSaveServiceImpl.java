package com.atjixue.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.atjixue.common.to.es.SkuEsModel;
import com.atjixue.gulimall.search.config.GulimallElasticSearchConfig;
import com.atjixue.gulimall.search.constant.EsConstant;
import com.atjixue.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author LiuJixue
 * @Date 2022/8/17 21:27
 * @PackageName:com.atjixue.gulimall.search.service.impl
 * @ClassName: ProductSaveServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Override
    public Boolean productStatusUp(List<SkuEsModel> skuEsModel) throws IOException {
        // 保存到es
        // 1、给es中建立索引，product 建立好应映射关系

        // 2、给se索引保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model : skuEsModel) {
            // 构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        // TODO 如果批量错误 我们可以处理错误
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map((item) -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.info("上架完成:{}","返回数据：{}",collect,bulk.toString());

        return b;
    }
}
