package com.zls.service;

import com.zls.pojo.Article;
import com.zls.pojo.Column;
import com.zls.pojo.Tag;
import entity.Page;
import org.apache.ibatis.annotations.Select;

import javax.servlet.http.HttpServletRequest;
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
     * 根据用户ID查询文章
     * @param userId 用户ID
     * @return
     */
    List<Article> findArticleByUserId(Integer userId);

    /**
     * 根据ID删除文章（做权限校验）
     * @param id 文章ID
     * @param userId 用户ID，作者
     * @param request
     * @return
     */
    boolean deleteArticle(Integer id , Integer userId, HttpServletRequest request);


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

    /**
     * 根据用户ID和专栏ID查询文章
     * @param userId
     * @param columnId
     * @return
     */
    List<Article> findArticleByUserIdAndColumnId(Integer userId,Integer columnId);

    /**
     * 查询排行前7的热门文章,并且放入缓存中
     * @return
     */
    List<Article> findHotArticle();
}
