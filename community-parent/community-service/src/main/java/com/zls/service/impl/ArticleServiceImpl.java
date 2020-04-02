package com.zls.service.impl;

import com.zls.mapper.ArticleMapper;
import com.zls.pojo.Article;
import com.zls.service.ArticleService;
import entity.Page;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.PageUtils;

import java.util.List;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询所有文章，以及排序
     * @param page 当前页
     * @param sort 排序规则
     * @return Page
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


    /**
     * 实现文章点赞功能
     * @param articleId  文章ID
     * @param liketor 点赞者ID
     */
    @Override
    public boolean likeArticle(Integer articleId, Integer liketor) {
        // 先从Redis中查找该用户有没有已经点赞了该文章
        Boolean flag = stringRedisTemplate.opsForValue().getBit("article:like_count:" + articleId, liketor);
        if (false){
            return false;
        }
        Boolean aBoolean = stringRedisTemplate.opsForValue().setBit("article:like_count:" + articleId, liketor, true);
        if (aBoolean){
            throw new CustomizeException(CustomizeErrorCode.LIKE_FAILED);
        }
        return true;
    }


    /**
     * 增加评论数+1
     * @param articleId 文章ID
     * @return
     */
    @Override
    public boolean incrementCommentCount(Integer articleId) {
        Boolean aBoolean = articleMapper.incrementCommentCount(articleId);
        if (aBoolean) {
            return true;
        }
        return false;
    }


    /**
     * 添加文章
     * @param article  文章实体
     * @return
     */
    @Override
    public boolean addArticle(Article article) {
        boolean b = articleMapper.addArticle(article);
        if (b) {
            return true;
        }
        return false;
    }
}
