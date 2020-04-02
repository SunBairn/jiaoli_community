package com.zls.mapper;

import com.zls.pojo.Article;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository public interface ArticleMapper {


    /**
     * 添加文章
     * @param article 文章实体
     * @return
     */
    @Insert("insert into tb_article (user_id, title, content, gmt_create, gmt_modified, url, tag, image_url) value " +
            "(#{article.userId} ,#{article.title} ,#{article.content} ," +
            "#{article.gmtCreate} ,#{article.gmtModified} ,#{article.url} ," +
            "#{article.tag} ,#{article.imageUrl} )")
    boolean addArticle(@Param("article") Article article);

    /**
     * 分页查询所有的文章，按时间降序排
     * @param start
     * @param size
     * @return List
     */

    @Select("select * from tb_article order by gmt_create desc limit #{start} ,#{size} ")
    List<Article> findAllArticle(@Param("start") Long start,@Param("size") int size);

    /**
     * 分页查询所有的文章(包含用户的部分信息)，按时间降序排
     * @param start
     * @param size
     * @return List
     */

    @Select("select * from tb_article order by gmt_create desc limit #{start} ,#{size} ")
    @Results({
            @Result(column = "user_id",property = "user",one = @One(select = "com.zls.mapper.UserMapper.findByIdPortion")),
    })
    List<Article> findAllArticleWithUser(@Param("start") Long start,@Param("size") int size);

    /**
     * 分页查询所有问题或帖子,按热度排序
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_article order by like_count desc limit #{start} ,#{size} ")
    List<Article> findAllArticleByHot(@Param("start") Long start,@Param("size") int size);


    /**
     * 分页查询所有问题或帖子(包含用户的部分信息),按热度排序
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_article order by like_count desc limit #{start} ,#{size} ")
    @Results({
            @Result(column = "user_id",property = "user",one = @One(select = "com.zls.mapper.UserMapper.findByIdPortion")),
    })
    List<Article> findAllArticleWithUserByHot(@Param("start") Long start,@Param("size") int size);


    /**
     * 统计文章的数量
     * @return Long
     */
    @Select("select count(1) from tb_article")
    Long articleCount();

    /**
     * 评论数+1操作
     * @param articleId 文章ID
     * @return
     */
    @Update("update tb_article set comment_count = comment_count+1 where id=#{articleId} ")
    Boolean incrementCommentCount(@Param("articleId") Integer articleId);
}
