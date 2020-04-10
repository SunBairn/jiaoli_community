package com.zls.service.impl;

import com.zls.mapper.ArticleCommentMapper;
import com.zls.pojo.ArticleComment;
import com.zls.pojo.QuestionComment;
import com.zls.service.ArticleCommentService;
import com.zls.service.ArticleService;
import com.zls.utils.RedisServiceExtend;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    RedisServiceExtend redisServiceExtend;

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
        stringRedisTemplate.expire("article:comment:like_count:" + commentId, 10, TimeUnit.DAYS);
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
        articleComment.setPublishdate(System.currentTimeMillis());
        boolean b = articleCommentMapper.addComment(articleComment);
        // 评论数 +1
        if (articleComment.getType() == 1) {
            boolean b1 = articleService.incrementCommentCount(articleComment.getParentId());
            if (!b1) {
                // 这里如果添加失败则会进行事务回滚
                throw new CustomizeException(CustomizeErrorCode.INCREMENT_COMMENT_COUNT_FAILED);
            }
        }else  if (articleComment.getType()==2) {
            // 如果为回复，则评论和对应的问题的回复数都 +1
            boolean b1 = articleService.incrementCommentCount(articleComment.getArticleId());
            if (!b1) {
                // 这里如果添加失败则会进行事务回滚
                throw new CustomizeException(CustomizeErrorCode.INCREMENT_COMMENT_COUNT_FAILED);
            }
            boolean b2 = articleCommentMapper.incrementReplyCount(articleComment.getParentId());
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

    /**
     * 分页查询评论（懒加载）
     * @param parentId 父类ID
     * @param type 所属类型
     * @param page 当前页
     * @return
     */
    @Override
    public List<ArticleComment> pageFindCommentWithUserByParentId(Integer parentId, Integer type, Integer page) {
        Integer start = page*10;
        List<ArticleComment> articleComments = articleCommentMapper.pageFindCommentWithUserByParentId(parentId, type, start);
        // 从缓存中读取点赞数
        for (ArticleComment articleComment  : articleComments){
            Integer id = articleComment.getId();
            Boolean aBoolean = stringRedisTemplate.hasKey("article:comment:like_count:" + id);
            if (aBoolean){
                articleComment.setLikeCount(redisServiceExtend.bitCount("article:comment:like_count:"+id));
            }
        }
        return articleComments;
    }


    /**
     * 根据 parentId 查询所有回复
     * @param parentId 父类ID
     * @return
     */
    @Override
    public List<ArticleComment> findAllReplyByParentId(Integer parentId) {
        List<ArticleComment> reply = articleCommentMapper.findCommentWithUserByParentId(parentId, 2);
        return reply;
    }
}
