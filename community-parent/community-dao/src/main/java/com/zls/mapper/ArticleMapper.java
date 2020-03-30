package com.zls.mapper;

import com.zls.pojo.Article;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository public interface ArticleMapper {

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
}
