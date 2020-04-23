package com.zls.controller;

import com.zls.service.SearchServiceImpl;
import entity.Result;
import entity.StatusCode;
import org.elasticsearch.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchServiceImpl searchService;

    /**
     * 分页查询搜索到的所有内容
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/all")
    public Result findAllByKeyword(@RequestParam("keyword") String keyword,
                                   @RequestParam(required = false,name = "page",defaultValue = "0") Integer page,
                                   @RequestParam(required = false,name = "size",defaultValue = "10") Integer size){
        Map<String, List> content = searchService.findAllByKeyword(keyword, page, size);
        if (content != null) {
            return new Result(true, StatusCode.OK, "查询成功！", content);
        }
        return new Result(false, StatusCode.ERROR, "查询数据为空！");
    }
}
