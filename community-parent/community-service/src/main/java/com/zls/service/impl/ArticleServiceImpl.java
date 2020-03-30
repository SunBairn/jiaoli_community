package com.zls.service.impl;

import com.zls.mapper.ArticleMapper;
import com.zls.pojo.Article;
import com.zls.service.ArticleService;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.PageUtils;

import java.util.List;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    /**
     * 分页查询所有文章，以及排序
     * @param page
     * @param sort
     * @return
     */

    @Override
    public Page<Article> findAllArticle(int page, String sort) {
        List<Article> articleList=null;
        Long total = articleMapper.articleCount();
        Page<Article> page1 = new Page<>(total, page, Page.pageSize);
        if(sort.equals("new")) {
            articleList = articleMapper.findAllArticleWithUser(page1.getStart(), page1.getSize());
        }
        if(sort.equals("hot")){
            articleList = articleMapper.findAllArticleWithUserByHot(page1.getStart(), page1.getSize());
        }
        page1.setList(articleList);
        return page1;
    }
}
