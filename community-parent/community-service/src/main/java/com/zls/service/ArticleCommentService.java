package com.zls.service;

import com.zls.pojo.ArticleComment;
import com.zls.pojo.QuestionComment;

import java.util.List;

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


    /**
     * 分页查询评论（懒加载）
     * @param parentId 父类ID
     * @param type 所属类型
     * @param page 当前页
     * @return
     */
    List<ArticleComment> pageFindCommentWithUserByParentId(Integer parentId, Integer type, Integer page);

    /**
     * 根据 parentId 查询所有回复
     * @param parentId 父类ID
     * @return
     */
    List<ArticleComment> findAllReplyByParentId(Integer parentId);


}
