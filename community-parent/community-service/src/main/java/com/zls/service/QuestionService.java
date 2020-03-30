package com.zls.service;

import com.zls.pojo.Question;
import entity.Page;

import java.util.List;

/**
 * 问题接口
 */
public interface QuestionService {

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
}
