package com.zls.service;

import com.zls.pojo.ArticleComment;
import com.zls.pojo.QuestionComment;

/**
 * 文章评论服务
 */
public interface ArticleCommentService {

    /**
     * 实现评论的(只有一级评论有)点赞功能
     * @param commentId  评论ID
     * @param liketor 点赞者ID
     */
    boolean likeComment(Integer commentId,Integer liketor);


    /**
     * 添加评论
     * @param articleComment 文章评论
     * @return
     */
    boolean addComment(ArticleComment articleComment);
}
