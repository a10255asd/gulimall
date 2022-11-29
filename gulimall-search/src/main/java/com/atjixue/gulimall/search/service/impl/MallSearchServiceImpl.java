package com.atjixue.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atjixue.common.to.es.SkuEsModel;
import com.atjixue.common.utils.R;
import com.atjixue.gulimall.search.config.GulimallElasticSearchConfig;
import com.atjixue.gulimall.search.constant.EsConstant;
import com.atjixue.gulimall.search.feign.ProductFeignService;
import com.atjixue.gulimall.search.service.MallSearchService;
import com.atjixue.gulimall.search.vo.AttrResponseVo;
import com.atjixue.gulimall.search.vo.BrandVo;
import com.atjixue.gulimall.search.vo.SearchParam;
import com.atjixue.gulimall.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author LiuJixue
 * @Date 2022/8/24 10:26
 * @PackageName:com.atjixue.gulimall.search.service.impl
 * @ClassName: MallSearchServiceOmpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    private RestHighLevelClient client;

    @Override
    public SearchResult search(SearchParam param) {
        // 1、 动态构建出查询需要的DSL语句
        SearchResult result = null;
        // 1、 准备检索请求

        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            // 2、执行检索请求
            SearchResponse response = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

            // 3、分析响应数据，封装成我们需要的格式
            result = buildSearchResult(response,param);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 构建结果数据
     */
    private SearchResult buildSearchResult(SearchResponse response,SearchParam param) {

        SearchResult result = new SearchResult();
        // 1、封装返回所有查询到的商品
        List<SkuEsModel> esModels = new ArrayList<>();
        SearchHits hits = response.getHits();
        if(hits.getHits()!= null&&hits.getHits().length >0){
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if(!StringUtils.isEmpty(param.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    esModel.setSkuTitle(string);
                }
                esModels.add(esModel);
            }
        }
        result.setProducts(esModels);

        // 2、 当前所有商品涉及到的所有属性信息
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            // 得到属性id
            long attrId = bucket.getKeyAsNumber().longValue();
            // 得到属性名字
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            // 得到属性值
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map((item) -> {
                String keyAsString = ((Terms.Bucket) item).getKeyAsString();
                return keyAsString;
            }).collect(Collectors.toList());

            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);

            attrVos.add(attrVo);
        }
        result.setAttrs(attrVos);
        // 3、 当前商品所涉及的品牌信息
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            // 得到品牌的id
            Long brandId = bucket.getKeyAsNumber().longValue();
            // 得到品牌的名字
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            // 得到品牌的图片
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandName(brandName);
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);
        // 4、当前商品所涉及到的所有分类信息
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            String keyAsString = bucket.getKeyAsString();
            // 得到分类id
            catalogVo.setCatalogId(Long.parseLong(keyAsString));
            // 得到分类名 获取子聚合
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }
        result.setCatalogs(catalogVos);
        //=========以上从聚合信息中获取=========
        // 5、分页信息
        // 分页信息-页码
        result.setPageNum(param.getPageNum());
        // 分页信息-总记录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        // 分页信息-总页码 11/2 =5 ...1
        int totalPages = (int) total % EsConstant.PRODUCT_PAGESIZE == 0
                ? (int) total / EsConstant.PRODUCT_PAGESIZE
                : ((int) total / EsConstant.PRODUCT_PAGESIZE + 1);
        result.setTotalPages(totalPages);
        List<Integer> pageNavs = new ArrayList<>();
        for (int t = 1; t < totalPages; t++) {
            pageNavs.add(t);
        }
        result.setPageNavs(pageNavs);
        // 构建面包屑导航的功能
        if(param.getAttrs()!= null&& param.getAttrs().size() >0){
            List<SearchResult.NavVo> collect = param.getAttrs().stream().map(attr -> {
                // 分析每一个attrs 查询的参数值
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.attrInfo(Long.parseLong(s[0]));
                result.getAttrIds().add(Long.parseLong(s[0]));
                if(r.getCode() == 0){
                    AttrResponseVo data = r.getData("attr", new TypeReference<AttrResponseVo>() {
                    });
                    navVo.setNavName(data.getAttrName());
                }else{
                    navVo.setNavName(s[0]);
                }
                // 取消面包屑以后， 我们要跳转到那个地方，将请求地址的url里面的url置空
                // 拿到所有的查询条件，去掉当前
                String replace = replaceQueryString(param, attr,"attrs");
                navVo.setLink("http://search.guliamll.com/list.html?" + replace);
                return navVo;
            }).collect(Collectors.toList());
            result.setNavs(collect);
        }
        // 品牌，分类
        if(param.getBrandId()!= null&& param.getBrandId().size()>0){
            List<SearchResult.NavVo> navs = result.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();
            navVo.setNavName("品牌");
            //TODO 远程查询所有品牌
            R r = productFeignService.bradnInfo(param.getBrandId());
            if(r.getCode() == 0){
                List<BrandVo> brand = r.getData("brand", new TypeReference<List<BrandVo>>() {
                });
                StringBuffer buffer = new StringBuffer();
                String replace = "";
                for (BrandVo brandVo : brand) {
                    buffer.append(brandVo.getBrandName() + ";");
                    replace = replaceQueryString(param,brandVo.getBrandId()+"","brandId");
                }
                navVo.setNavValue(buffer.toString());
                navVo.setLink("http://search.guliamll.com/list.html?" + replace);
            }
            navs.add(navVo);
        }
        // TODO 分类不需要导航取消

        return result;
    }

    private String replaceQueryString(SearchParam param, String value,String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            // 浏览器对空格编码和java不一样
            encode =  encode.replace("+","%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String replace = param.get_queryString().replace("&" + key +"=" + encode, "");
        return replace;
    }

    /**
     * 准备检索请求
     * 模糊匹配，过滤（按照属性，分类，品牌，价格区间，库存），排序，分页，高亮，聚合分析
     */
    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        /**
         * 模糊匹配，过滤（按照属性，分类，品牌，价格区间，库存）
         * */
        //构建bool -query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 1.1must
        if (!StringUtils.isEmpty(param.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        // 1.2、bool filter 按照三级分类id查
        if (param.getCatalog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }
        // 1.2  品牌id进行查询
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        // 1.2  按照所有指定的属性进行查询
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {

            for (String attrString : param.getAttrs()) {
                // attrs=1_5寸:8寸&attrs:2_16G:8G
                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
                // attr= attrs=1_5寸:8寸
                String[] s = attrString.split("_");
                String attrId = s[0]; // 检索的属性id
                String[] attrValues = s[1].split(":"); //这个属性的检索用的值
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrValue", attrValues));
                // 每一个必须都得生成一个nested查询
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedboolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }

        }
        // 1.2 按照库存是否有进行查询
        if(param.getHasStock() != null){
            boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock()  == 1));
        }
        // 1.2 按照价格区间进行检索
        if (!StringUtils.isEmpty(param.getSkuPrice())) {
            // 1_500 _500 500_
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_");
            if (s.length == 2) {
                // 区间
                rangeQuery.gte(s[0]).lte(s[1]);
            } else if (s.length == 1) {
                if (param.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(s[1]);
                }
                if (param.getSkuPrice().endsWith("_")) {
                    rangeQuery.gte(s[0]);
                }
            }
            boolQuery.filter(rangeQuery);
        }

        // 把以前的所有条件都拿来进行封装
        sourceBuilder.query(boolQuery);
        /**
         * 排序，分页，高亮，
         * */
        //2.1 排序
        if (!StringUtils.isEmpty(param.getSort())) {
            String sort = param.getSort();
            String[] s = sort.split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(s[0], order);
        }
        //2.2 分页 pageSize :5
        // pageNum:1 from 0 size 5 [0,1,2,3,4]
        // pageNum:2 from 5 size 5
        // from = (pageNum-1) * size
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
        // 2.3 高亮
        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");
            sourceBuilder.highlighter(builder);
        }
        /**
         * 聚合分析
         * */
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        // 品牌聚合的子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        // TODO 1、聚合品牌的信息
        sourceBuilder.aggregation(brand_agg);
        // 分类聚合 catalog_agg
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        // TODO 2、聚合分类的信息
        sourceBuilder.aggregation(catalog_agg);

        // 属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        // 聚合出当前所有的attr_id
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        // 聚合分析出attr_id 对应的名字
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        // 聚合分析出attr_id 对应的所有可能的属性值 attrValue
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        // TODO 3、聚合属性的信息
        sourceBuilder.aggregation(attr_agg);

        String s = sourceBuilder.toString();
        System.out.println("构建的DSL" + s);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return searchRequest;
    }
}
