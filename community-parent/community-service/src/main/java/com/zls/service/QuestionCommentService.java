package com.zls.service;

import com.zls.pojo.QuestionComment;

import java.util.List;

/**
 * 问题（帖子）评论服务
 */
public interface QuestionCommentService {

    /**
     * 分页查询评论（懒加载）
     * @param parentId 父类ID
     * @param type 所属类型
     * @param page 当前页
     * @return
     */
    List<QuestionComment> pageFindCommentWithUserByParentId(Integer parentId,Integer type,Integer page);

    /**
     * 根据 parentId 查询所有回复
     * @param parentId 父类ID
     * @return
     */
    List<QuestionComment> findAllReplyByParentId(Integer parentId);

    /**
     *统计某个问题或帖子的总评论数
     * @param parentId  父类ID
     * @return
     */
    Long getCountByParentId(Integer parentId);


    /**
     * 实现评论(只有一级评论有)点赞功能
     * @param commentId  评论ID
     * @param liketor 点赞者ID
     */
    boolean likeComment(Integer commentId,Integer liketor);

    /**
     * 添加评论
     * @param questionComment 问题评论
     * @return
     */
    boolean addComment(QuestionComment questionComment);
}
