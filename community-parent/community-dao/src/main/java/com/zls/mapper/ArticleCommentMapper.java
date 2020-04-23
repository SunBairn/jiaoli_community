package com.zls.mapper;

import com.zls.pojo.ArticleComment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章评论mapper层
 */
@Mapper
@Repository public interface ArticleCommentMapper {


    /**
     * 添加文章评论
     * @param articleComment
     * @return
     */
    @Insert("insert into tb_article_comment (content, user_id, parent_id, publishdate, type, article_id) value (" +
            "#{articleComment.content} ,#{articleComment.userId}, #{articleComment.parentId} ," +
            "#{articleComment.publishdate} ,#{articleComment.type},#{articleComment.articleId} )")
    boolean addComment(@Param("articleComment") ArticleComment articleComment);


    /**
     * 根据parent_id查询所有评论(按时间倒序)（包含部分用户信息）
     * @param parentId
     * @return
     */
    @Select("select * from tb_article_comment where parent_id=#{parentId} and type=#{type}")
    @Results({
            @Result(column = "user_id",property = "user",one =@One(select = "com.zls.mapper.UserMapper.findByIdPortion"))
    })
    List<ArticleComment> findCommentWithUserByParentId(@Param("parentId") Integer parentId, @Param("type") Integer type);


    /**
     * 根据parent_id分页查询评论默认10个(按时点赞数倒序)（包含部分用户信息）
     * @param parentId
     * @return
     */
    @Select("select * from tb_article_comment where parent_id=#{parentId}  and type=#{type}  order by like_count desc limit #{start} ,10")
    @Results({
            @Result(column = "user_id",property = "user",one =@One(select = "com.zls.mapper.UserMapper.findByIdPortion"))
    })
    List<ArticleComment> pageFindCommentWithUserByParentId(@Param("parentId") Integer parentId,@Param("type") Integer type,@Param("start") Integer start);

    /**
     * 给评论回复数 +1
     * @param commentId 评论ID
     * @return
     */
    @Update("update tb_article_comment set reply_count=reply_count+1 where id=#{commentId} ")
    boolean incrementReplyCount(@Param("commentId") Integer commentId);
}
