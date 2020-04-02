package com.zls.service.impl;

import com.zls.mapper.ArticleCommentMapper;
import com.zls.pojo.ArticleComment;
import com.zls.service.ArticleCommentService;
import com.zls.service.ArticleService;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章评论服务
 */
@Service
@Transactional
public class ArticleCommentServiceImpl implements ArticleCommentService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ArticleCommentMapper articleCommentMapper;

    @Autowired
    ArticleService articleService;

    /**
     * 文章评论点赞功能
     * @param commentId  评论ID
     * @param liketor 点赞者ID
     * @return
     */
    @Override
    public boolean likeComment(Integer commentId, Integer liketor) {
        // 先从Redis中查找该用户有没有已经点赞了该评论
        Boolean flag = stringRedisTemplate.opsForValue().getBit("article:comment:like_count:" + commentId, liketor);
        if (false){
            return false;
        }
        Boolean aBoolean = stringRedisTemplate.opsForValue().setBit("article:comment:like_count:" + commentId, liketor, true);
        if (aBoolean){
            throw new CustomizeException(CustomizeErrorCode.LIKE_FAILED);
        }
        return true;
    }

    /**
     * 添加文章的评论
     * @param articleComment 文章评论
     * @return
     */
    @Override
    public boolean addComment(ArticleComment articleComment) {
        boolean b = articleCommentMapper.addComment(articleComment);
        boolean b1 = articleService.incrementCommentCount(articleComment.getParentId());
        if (b&&b1) {
            return true;
        }
        throw new CustomizeException(CustomizeErrorCode.INCREMENT_COMMENT_COUNT_FAILED);
    }
}
