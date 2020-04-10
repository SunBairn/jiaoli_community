package com.zls.service;

import com.zls.pojo.Question;
import entity.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 问题（或贴子）接口
 */
public interface QuestionService {


    /**
     * 添加问题
     * @param question 问题实体
     * @return
     */
    boolean addQuestion(Question question,Integer type);

    /**
     * 修改问题或帖子
     * @param question 问题实体
     * @return
     */
    boolean updateQuestion(Question question);

    /**
     * 分页查询所有问题或者帖子
     * @return
     */
    Page<Question> findAllQuestion(Integer type, int page,String sort);

    /**
     * 统计问题或帖子的数量
     * @param type
     * @return
     */
    Long questionCount(Integer type);

    /**
     * 根据ID查询问题，评论，用户
     * @param id
     * @return
     */
    Question findQuestionWithUserWithCommentById(Integer id);


    /**
     * 实现问题（帖子）点赞功能
     * @param questionId  问题ID
     * @param liketor 点赞者ID
     */
    boolean likeQuestion(Integer questionId,Integer liketor);


    /**
     * 增加评论数
     * @param questionId 问题ID
     * @return
     */
    boolean incrementCommentCount(Integer questionId);

    /**
     * 根据Creator和type查询问题或帖子
     * @param creator 作者ID
     * @param type 类型
     * @return
     */
    List<Question> findQuestionByCreatorAndType(Integer creator,Integer type);

    /**
     * 根据ID删除问题或帖子
     * @param id questionId
     * @return
     */
    boolean deleteQuestion(Integer id,Integer userId,Integer creator, HttpServletRequest request);

}
