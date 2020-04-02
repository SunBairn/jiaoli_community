package com.zls.service;

import com.zls.pojo.Article;
import entity.Page;

public interface ArticleService {

    /**
     * 分页查询所有文章，以及排序
     * @param page 当前页
     * @param sort 排序规则
     * @return Page<Article>
     */
    Page<Article> findAllArticle(int page,String sort);

    /**
     * 实现文章点赞功能
     * @param articleId  文章ID
     * @param liketor 点赞者ID
     */
    boolean likeArticle(Integer articleId,Integer liketor);


    /**
     * 增加评论数
     * @param articleId 文章ID
     * @return
     */
    boolean incrementCommentCount(Integer articleId);


    /**
     * 添加文章
     * @param article  文章实体
     * @return
     */
    boolean addArticle(Article article);
}
