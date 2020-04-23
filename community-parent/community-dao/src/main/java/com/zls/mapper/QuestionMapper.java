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
    boolean addQuestion(Question question);


    /**
     * 修改文章或帖子
     * @param question 问题实体
     * @return
     */
    @Update("update tb_question set title=#{question.title} ,content=#{question.content} ," +
            "gmt_modified = #{question.gmtModified} " +
            "where id=#{question.id}")
    boolean updateQuestion(@Param("question") Question question);

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
     * 分页查询所有问题或帖子,条件为：回复数为0的问题
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
     * 根据用户的ID和问题的type查询所有的问题或帖子
     * @param creator
     * @return
     */
    @Select("select id,title,content,creator,gmt_create,view_count,like_count,comment_count from tb_question where creator=#{creator} and type=#{type} order by gmt_modified desc")
    List<Question> findQuestionByCreatorAndType(@Param("creator") Integer creator,@Param("type") Integer type);


    /**
     * 根据用户ID查询所有的问题和帖子
     * @param creator
     * @return
     */
    @Select("select id,title,creator,gmt_create,view_count,like_count,comment_count from tb_question where creator=#{creator}  order by gmt_modified desc")
    List<Question> findQuestionByCreator(@Param("creator") Integer creator);



    /**
     * 根据问题ID查询某个问题的具体信息（包含用户的部分信息）
     * @param id
     * @return
     */
    @Select("select * from tb_question where id=#{id}")
    @Results({
            @Result(column = "id",property = "id",id = true),
            @Result(column = "creator",property = "user",one = @One(select = "com.zls.mapper.UserMapper.findByIdPortion")),
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
     * 根据ID查询阅读数(用于判定是否从数据库中取放入缓存中)
     * @Param id
     */
    @Select("select view_count from tb_question where id = #{id}")
    Integer getViewCount(@Param("id") Integer id);

    /**
     * 同步阅读数到数据库中
     * @param map
     */
    Boolean syncViewCountToDatabase(@Param("map") Map<Integer,Integer> map);


    /**
     * 评论数+1操作
     * @param questionId 问题ID
     * @return
     */
    @Update("update tb_question set comment_count = comment_count+1 where id=#{questionId} ")
    Boolean incrementCommentCount(@Param("questionId") Integer questionId);

    /**
     * 根据ID删除问题或帖子
     * @param id questionId
     * @return
     */
    @Delete("delete from tb_question where id=#{Id}")
    boolean deleteQuestion(@Param("Id") Integer id);

    /**
     * 根据用户ID查询问题数量
     * @param creator 用户ID
     * @return
     */
    @Select("select count(*) from tb_question where creator=#{creator} and type=2")
    Integer findQuestionCountByCreator(@Param("creator") Integer creator);

    /**
     * 根据用户ID查询帖子数量
     * @param creator 用户ID
     * @return
     */
    @Select("select count(*) from tb_question where creator=#{creator} and type=1")
    Integer findInvitationCountByCreator(@Param("creator") Integer creator);
}
