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
     * 根据parent_id查询评论（包含部分用户信息）
     * @param parentId
     * @return
     */
    @Select("select * from tb_question_comment where parent_id=#{parentId} and type=1")
    @Results({
            @Result(column = "commentator",property = "user",one =@One(select = "com.zls.mapper.UserMapper.findByIdPortion"))
    })
    List<QuestionComment> findCommentWithUserById(@Param("parentId") Integer parentId);

}
