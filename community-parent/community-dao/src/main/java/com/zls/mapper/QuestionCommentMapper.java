package com.zls.mapper;

import com.zls.pojo.QuestionComment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问题评论mapper层
 */
@Mapper
@Repository public interface QuestionCommentMapper {

    /**
     * 根据parent_id查询所有评论(按时间倒序)（包含部分用户信息）
     * @param parentId
     * @return
     */
    @Select("select * from tb_question_comment where parent_id=#{parentId} and type=#{type}")
    @Results({
            @Result(column = "commentator",property = "user",one =@One(select = "com.zls.mapper.UserMapper.findByIdPortion"))
    })
    List<QuestionComment> findCommentWithUserByParentId(@Param("parentId") Integer parentId,@Param("type") Integer type);


    /**
     * 根据parent_id分页查询评论默认10个(按时点赞数倒序)（包含部分用户信息）
     * @param parentId
     * @return
     */
    @Select("select * from tb_question_comment where parent_id=#{parentId}  and type=#{type}  order by like_count desc limit #{start} ,10")
    @Results({
            @Result(column = "commentator",property = "user",one =@One(select = "com.zls.mapper.UserMapper.findByIdPortion"))
    })
    List<QuestionComment> pageFindCommentWithUserByParentId(@Param("parentId") Integer parentId,@Param("type") Integer type,@Param("start") Integer start);


    /**
     * 统计某个问题或帖子的总评论数
     * @param parentId 父类ID
     */
    @Select("select count(1) from tb_question_comment where parent_id = #{parentId}  ")
    Long getCountByParentId(@Param("parentId") Integer parentId);
}
