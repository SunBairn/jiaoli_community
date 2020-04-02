package com.zls.service.impl;

import com.zls.mapper.QuestionCommentMapper;
import com.zls.pojo.QuestionComment;
import com.zls.service.QuestionCommentService;
import com.zls.service.QuestionService;
import com.zls.utils.RedisServiceExtend;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class QuestionCommentServiceImpl implements QuestionCommentService {
    @Autowired
    QuestionCommentMapper questionCommentMapper;

    @Autowired
    QuestionService questionService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisServiceExtend redisServiceExtend;
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
        for (QuestionComment questionComment : questionComments){
            Integer id = questionComment.getId();
            Boolean aBoolean = stringRedisTemplate.hasKey("question:comment:like_count:" + id);
            if (aBoolean){
                questionComment.setLikeCount(redisServiceExtend.bitCount("question:comment:like_count:"+id));
            }
        }
        return questionComments;
    }

    /**
     * 根据父类ID查询所有回复
     * @param parentId 父类ID
     * @return
     */
    @Override
    public List<QuestionComment> findAllReplyByParentId(Integer parentId) {
        List<QuestionComment> reply = questionCommentMapper.findCommentWithUserByParentId(parentId, 2);
        return reply;
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



    /**
     * 实现评论的(只有一级评论有)点赞功能
     * @param commentId  评论ID
     * @param liketor 点赞者ID
     */
    @Override
    public boolean likeComment(Integer commentId, Integer liketor) {
        // 先从Redis中查找该用户有没有已经点赞了该评论
        Boolean flag = stringRedisTemplate.opsForValue().getBit("question:comment:like_count:" + commentId, liketor);
        if (flag){
            return false;
        }
        Boolean aBoolean = stringRedisTemplate.opsForValue().setBit("question:comment:like_count:" + commentId, liketor, true);
        if (aBoolean){
            throw new CustomizeException(CustomizeErrorCode.LIKE_FAILED);
        }
        return true;
    }


    /**
     * 添加问题评论(或者回复)
     * @param questionComment 问题评论
     * @return
     */
    @Override
    public boolean addComment(QuestionComment questionComment) {
        questionComment.setGmtCreate(System.currentTimeMillis());
        boolean b = questionCommentMapper.addComment(questionComment);
        // 问题评论数+1
        if (questionComment.getType() == 1) {
            boolean b1 = questionService.incrementCommentCount(questionComment.getParentId());
            if (!b1) {
                // 这里如果添加失败则会进行事务回滚
                throw new CustomizeException(CustomizeErrorCode.INCREMENT_COMMENT_COUNT_FAILED);
            }
        }else  if (questionComment.getType()==2) {
            // 如果为回复，则评论和对应的问题的回复数 +1
            boolean b1 = questionService.incrementCommentCount(questionComment.getQuestionId());
            if (!b1) {
                // 这里如果添加失败则会进行事务回滚
                throw new CustomizeException(CustomizeErrorCode.INCREMENT_COMMENT_COUNT_FAILED);
            }
            boolean b2 = questionCommentMapper.incrementReplyCount(questionComment.getParentId());
            if (!b2) {
                throw new CustomizeException(CustomizeErrorCode.INCREMENT_REPLY_COUNT_FAILED);
            }
        }else{
            throw new CustomizeException(CustomizeErrorCode.INSERT_COMMENT_FAILED);
        }

        if (b){
            return true;
        }
        return false;
    }
}
