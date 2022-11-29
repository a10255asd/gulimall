package com.atjixue.gulimall.search.controller;

import com.atjixue.gulimall.search.service.MallSearchService;
import com.atjixue.gulimall.search.vo.SearchParam;
import com.atjixue.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author LiuJixue
 * @Date 2022/8/24 00:35
 * @PackageName:com.atjixue.gulimall.search.controller
 * @ClassName: SearchController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
public class SearchController {
    @Autowired
    MallSearchService mallSearchService;

    /**
     * 自动将页面提交过来的所有请求参数封装成，指定的对象
     * */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request){
        param.set_queryString(request.getQueryString());
        //1、根据传递来的页面参数，去es中检索产品
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result",result);
        return "list";
    }
}
