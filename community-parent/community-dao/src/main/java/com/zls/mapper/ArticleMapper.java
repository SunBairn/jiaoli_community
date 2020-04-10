package com.zls.mapper;

import com.zls.pojo.Article;
import com.zls.pojo.Column;
import com.zls.pojo.Tag;
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
    @Insert("insert into tb_article (user_id,column_id ,title, content, gmt_create, gmt_modified, url, tag, image_url) value " +
            "(#{article.userId},#{article.columnId} ,#{article.title} ,#{article.content} ," +
            "#{article.gmtCreate} ,#{article.gmtModified} ,#{article.url} ," +
            "#{article.tag} ,#{article.imageUrl} )")
    boolean addArticle(@Param("article") Article article);

    /**
     * 修改文章
     * @param article  文章实体
     * @return
     */
    @Update("update tb_article set title=#{article.title} , content=#{article.content} , tag=#{article.tag}," +
            "column_id=#{article.columnId} ,gmt_modified=#{article.gmtModified} ,image_url=#{article.imageUrl} " +
            " where id=#{article.id}")
    boolean updateArticle(@Param("article") Article article);

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
     * 分页查询所有文章,按热度排序
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_article order by like_count desc limit #{start} ,#{size} ")
    List<Article> findAllArticleByHot(@Param("start") Long start,@Param("size") int size);


    /**
     * 分页查询所有文章(包含用户的部分信息),按热度排序
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
     * 根据ID查询文章以及部分用户信息和所属专栏的名称
     * @param id 文章ID
     * @return
     */
    @Select("select * from tb_article where id=#{id}")
    @Results({
            @Result(column = "user_id", property = "user",one = @One(select = "com.zls.mapper.UserMapper.findByIdPortion")),
            @Result(column = "column_id", property = "column",one = @One(select = "com.zls.mapper.ColumnMapper.findNameById"))
    })
    Article findArticleWithUserById(@Param("id") Integer id);


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

    /**
     * 查询state为 1 的标签
     * @return
     */
    @Select("select id, tagname from tb_tag where state=1")
    List<Tag> findTagByState();

    /**
     * 查询state为 1 的专栏
     * @return
     */
    @Select("select id, name from tb_column where state=1")
    List<Column> findColumnByState();

    /**
     * 根据ID查询阅读数(用于判定是否从数据库中取放入缓存中)
     * @Param id
     */
    @Select("select view_count from tb_article where id = #{id}")
    Integer getViewCount(@Param("id") Integer id);
}
