package com.zls.service;

import com.zls.pojo.Article;
import com.zls.pojo.Column;
import com.zls.pojo.Tag;
import entity.Page;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 修改文章
     * @param article 文章实体
     * @return Boolean
     */
    boolean updateArticle(Article article);

    /**
     * 根据ID查询专栏的信息展示在具体页面
     */
    Article findArticleWithUserById(Integer id);


    /**
     * 查询state为 1 的标签
     * @return
     */
    List<Tag> findTagByState();

    /**
     * 查询state为 1 的专栏
     * @return
     */
    List<Column> findColumnBystate();


}
