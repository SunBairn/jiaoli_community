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
     *统计某个问题或帖子的总评论数
     * @param parentId  父类ID
     * @return
     */
    Long getCountByParentId(Integer parentId);
}
