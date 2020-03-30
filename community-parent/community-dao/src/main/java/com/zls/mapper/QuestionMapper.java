package com.zls.mapper;

import com.zls.pojo.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository public interface QuestionMapper {

    /**
     * 添加问题或帖子
     * @param question
     */
    @Insert("INSERT into tb_question (title, content, gmt_create, gmt_modified, creator, type) " +
            " value (#{title},#{content} ,#{gmtCreate} ,#{gmtModified} ," +
            "#{creator} ,#{type} )")
    void insertQuestion(Question question);

    /**
     * 分页查询所有问题或帖子,按时间倒序排序
     * @param type
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_question where type=#{type} order by gmt_create desc limit #{start} ,#{size}  ")
    List<Question> findAllQuestion(@Param("type") Integer type, @Param("start")Long start,@Param("size") int size);

    /**
     * 分页查询所有问题或帖子(包含用户的部分信息),按时间倒序排序
     * @param type
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_question where type=#{type} order by gmt_create desc limit #{start} ,#{size}")
    @Results({
            @Result(column = "creator",property = "user",one = @One(select = "com.zls.mapper.UserMapper.findByIdPortion")),
    })
    List<Question> findAllQuestionWithUser(@Param("type") Integer type, @Param("start")Long start,@Param("size") int size);



    /**
     * 分页查询所有问题或帖子,按热度排序
     * @param type
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_question where type=#{type} order by like_count desc limit #{start} ,#{size}  ")
    List<Question> findAllQuestionByHot(@Param("type") Integer type, @Param("start")Long start,@Param("size") int size);

    /**
     * 分页查询所有问题或帖子(包含用户的部分信息),按热度排序
     * @param type
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_question where type=#{type} order by like_count desc limit #{start} ,#{size}  ")
    @Results({
            @Result(column = "creator",property = "user",one = @One(select = "com.zls.mapper.UserMapper.findByIdPortion")),
    })
    List<Question> findAllQuestionWithUserByHot(@Param("type") Integer type, @Param("start")Long start,@Param("size") int size);

    /**
     * 分页查询所有问题或帖子,条件为回复数为0的问题
     * @param type
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_question where type=#{type} and comment_count=0 limit #{start} ,#{size}  ")
    List<Question> findAllQuestionByAwait(@Param("type") Integer type, @Param("start")Long start,@Param("size") int size);


    /**
     * 分页查询所有问题或帖子(包含用户的部分信息),条件为回复数为0的问题
     * @param type
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_question where type=#{type} and comment_count=0 limit #{start} ,#{size}  ")
    @Results({
            @Result(column = "creator",property = "user",one = @One(select = "com.zls.mapper.UserMapper.findByIdPortion")),
    })
    List<Question> findAllQuestionWithUserByAwait(@Param("type") Integer type, @Param("start")Long start,@Param("size") int size);




    /**
     * 根据用户的ID查询所有的问题或帖子
     * @param creator
     * @return
     */
    @Select("select * from tb_question where creator=#{creator} ")
    List<Question> findAllQuestionByCreator(@Param("creator") Integer creator);


    /**
     * 根据问题ID查询某个问题的具体信息和评论信息（只查一级评论）（包含用户的部分信息）
     * @param id
     * @return
     */
    @Select("select * from tb_question where id=#{id}")
    @Results({
            @Result(column = "id",property = "id",id = true),
            @Result(column = "creator",property = "user",one = @One(select = "com.zls.mapper.UserMapper.findByIdPortion")),
            @Result(column = "id",property = "questionCommentList",many = @Many(select = "com.zls.mapper.QuestionCommentMapper.findCommentWithUserById"))
    })
    Question findQuestionWithUserWithCommentById(@Param("id") Integer id);

    /**
     * 统计问题或帖子的数量
     * @param type
     * @return
     */
    @Select("select count(1) from tb_question where type=#{type}")
    Long questionCount(@Param("type") Integer type);


    /**
     * 同步点赞数到数据库中
     * @param map
     */
    Boolean syncViewCountToDatabase(@Param("map") Map<Integer,Integer> map);

}
