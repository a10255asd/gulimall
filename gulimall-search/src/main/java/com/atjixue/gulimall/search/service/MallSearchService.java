package com.atjixue.gulimall.search.service;

import com.atjixue.gulimall.search.vo.SearchParam;
import com.atjixue.gulimall.search.vo.SearchResult;

/**
 * @Author LiuJixue
 * @Date 2022/8/24 10:25
 * @PackageName:com.atjixue.gulimall.search.service
 * @ClassName: MallSearchService
 * @Description: TODO
 * @Version 1.0
 */
public interface MallSearchService {

    /**
     * @param param 检索的所有参数
     * @return 检索的结果
     */
    SearchResult search(SearchParam param);
}
