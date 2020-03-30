package com.zls.service.impl;

import com.zls.mapper.QuestionCommentMapper;
import com.zls.pojo.QuestionComment;
import com.zls.service.QuestionCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class QuestionCommentServiceImpl implements QuestionCommentService {
    @Autowired
    QuestionCommentMapper questionCommentMapper;

    /**
     * 根据parent_id分页查询评论默认10个(按时点赞数倒序)（包含部分用户信息）
     * @param parentId 父类ID
     * @param type 所属类型
     * @param page 当前页
     * @return
     */
    @Override
    public List<QuestionComment> pageFindCommentWithUserByParentId(Integer parentId,Integer type,Integer page) {
        Integer start = page*10;
        List<QuestionComment> questionComments = questionCommentMapper.pageFindCommentWithUserByParentId(parentId,type,start);
        return questionComments;
    }

    /**
     * 统计某个问题或帖子的总评论数
     * @param parentId  父类ID
     * @return
     */
    @Override
    public Long getCountByParentId(Integer parentId) {
        Long count = questionCommentMapper.getCountByParentId(parentId);
        return count;
    }
}
