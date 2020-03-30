package com.zls.controller;

import com.zls.pojo.Article;
import com.zls.service.ArticleService;
import entity.Page;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.PageUtils;

@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    /**
     * 分页查询所有文章，以及排序
     * @param page
     * @param sort
     * @return
     */
    @GetMapping("/find")
    public Result findAllArticle(@RequestParam(name = "page",defaultValue = "1") int page
            ,@RequestParam(name = "sort",defaultValue = "new") String sort){
        Page<Article> allArticle = articleService.findAllArticle(page, sort);
        PageResult pageResult = PageUtils.packagingPageResult(allArticle);
        return  new Result(true,20000,"查询成功",pageResult);
    }

}
