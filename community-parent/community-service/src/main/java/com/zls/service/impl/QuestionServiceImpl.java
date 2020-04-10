package com.zls.service.impl;

import com.zls.mapper.QuestionMapper;
import com.zls.pojo.Question;
import com.zls.service.QuestionService;
import com.zls.utils.RedisServiceExtend;
import entity.Page;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Repository public class QuestionServiceImpl implements QuestionService {


    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisServiceExtend redisServiceExtend;

    /**
     * 添加问题
     * @param question 问题实体
     * @return
     */
    @Override
    public boolean addQuestion(Question question , Integer type) {
        question.setType(type);
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(System.currentTimeMillis());
        boolean b = questionMapper.addQuestion(question);
        if (b) {
            return true;
        }
        return false;
    }

    /**
     * 修改问题或实体
     * @param question 问题实体
     * @return
     */
    @Override
    public boolean updateQuestion(Question question) {
        question.setGmtModified(System.currentTimeMillis());
        boolean b = questionMapper.updateQuestion(question);
        if (b) {
            return true;
        }
        return false;
    }

    /**
     * 分页查询所有,根据传过来的sort参数进行指定排序
     * @param type
     * @param page
     * @param sort
     * @return
     */
    @Override
    public Page<Question> findAllQuestion(Integer type,int page,String sort) {
        List<Question> allQuestion=null;
        Long total = questionMapper.questionCount(type);
        Page<Question> page1 = new Page<>(total,page,Page.pageSize);
        if(sort.equals("new")) {
            allQuestion = questionMapper.findAllQuestionWithUser(type, page1.getStart(), Page.pageSize);
            for(Question question:allQuestion){
                // 查询阅读数在缓存中有没有
                String s = stringRedisTemplate.opsForValue().get("question:view_count:" + question.getId());
                if (s!=null) {
                    question.setViewCount(Integer.valueOf(s));
                }
                // 查询点赞数在缓存中有没有
                Boolean aBoolean = stringRedisTemplate.hasKey("question:like_count:" + question.getId());
                if (aBoolean) {
                    question.setLikeCount(redisServiceExtend.bitCount("question:like_count:"+question.getId()));
                }
            }
        }
        if(sort.equals("hot")){
            allQuestion=questionMapper.findAllQuestionWithUserByHot(type,page1.getStart(),Page.pageSize);
            for(Question question:allQuestion){
                String s = stringRedisTemplate.opsForValue().get("question:view_count:" + question.getId());
                if (s!=null) {
                    question.setViewCount(Integer.valueOf(s));
                }
                // 查询点赞数在缓存中有没有
                Boolean aBoolean = stringRedisTemplate.hasKey("question:like_count:" + question.getId());
                if (aBoolean) {
                    question.setLikeCount(redisServiceExtend.bitCount("question:like_count:"+question.getId()));
                }
            }
        }
        if(sort.equals("await")){
            allQuestion=questionMapper.findAllQuestionWithUserByAwait(type,page1.getStart(),Page.pageSize);
            // 循环遍历缓存中有没有阅读数
            for(Question question:allQuestion){
                String s = stringRedisTemplate.opsForValue().get("question:view_count:" + question.getId());
                if (s!=null) {
                    question.setViewCount(Integer.valueOf(s));
                }
                // 查询点赞数在缓存中有没有
                Boolean aBoolean = stringRedisTemplate.hasKey("question:like_count:" + question.getId());
                // 有的话返回false
                if (aBoolean) {
                    question.setLikeCount(redisServiceExtend.bitCount("question:like_count:"+question.getId()));
                }
            }
        }
        page1.setList(allQuestion);
        page1.setTotal(total);
        return page1;
    }

    /**
     * 统计问题或帖子的数量
     * @param type
     * @return
     */
    @Override
    public Long questionCount(Integer type) {
        return questionMapper.questionCount(type);
    }


    /**
     * 根据ID查询问题（或帖子），评论，用户并统计阅读数和点赞数
     * @param id  questionID
     * @return
     */
    public Question findQuestionWithUserWithCommentById(Integer id){
        Question question = questionMapper.findQuestionWithUserWithCommentById(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        // 实现阅读数+1，采用Redis实现
        // 1、查询缓存中是否有阅读记录
        String s = stringRedisTemplate.opsForValue().get("question:view_count:" + id);
        if (StringUtils.isEmpty(s)) {
            // 如果缓存中没有，或者过期了，则从数据库中读取再添加到redis中
            Integer viewCount = questionMapper.getViewCount(id);
            stringRedisTemplate.opsForValue().set("question:view_count:"+id, String.valueOf(viewCount),20, TimeUnit.DAYS);
            stringRedisTemplate.opsForValue().increment("question:view_count:"+id);
        }else{
            stringRedisTemplate.opsForValue().increment("question:view_count:"+id);
        }
        // 避免出现EOF异常
        String v = stringRedisTemplate.boundValueOps("question:view_count:" + id).get(0,-1);
        // 将缓存中的阅读数返回给用户
        question.setViewCount(Integer.valueOf(v));
        // 将缓存中点赞数返回给用户
        Integer integer = redisServiceExtend.bitCount("question:like_count:" + id);
        if (integer!=null){
            question.setLikeCount(integer);
        }
        return question;
    }

    /**
     *  实现问题（帖子）点赞功能
     * @param questionId  问题ID
     * @param liketor 点赞者ID
     */
    @Override
    public boolean likeQuestion(Integer questionId, Integer liketor) {
        // 先从Redis中查找该用户有没有已经点赞了该问题或帖子
        Boolean flag = stringRedisTemplate.opsForValue().getBit("question:like_count:" + questionId, liketor);
        if (flag){
          return false;
        }
        Boolean aBoolean = stringRedisTemplate.opsForValue().setBit("question:like_count:" + questionId, liketor, true);
        stringRedisTemplate.expire("question:like_count:" + questionId, 30, TimeUnit.DAYS);
        if (aBoolean){
            throw new CustomizeException(CustomizeErrorCode.LIKE_FAILED);
        }
        return true;
    }

    /**
     * 增加评论数+1
     * @param questionId 问题ID
     * @return
     */
    @Override
    public boolean incrementCommentCount(Integer questionId) {
        Boolean aBoolean = questionMapper.incrementCommentCount(questionId);
        if (aBoolean) {
            return true;
        }
        return false;
    }

    /**
     * 根据creator和type查询问题或帖子
     * @param creator 作者ID
     * @param type 类型
     * @return
     */
    @Override
    public List<Question> findQuestionByCreatorAndType(Integer creator, Integer type) {
        List<Question> questions = questionMapper.findQuestionByCreatorAndType(creator, type);
        return questions;
    }

    /**
     * 根据ID删问题或帖子
     * @param id questionId
     * @param userId 用户ID
     * @param creator 问题作者
     * @param request
     * @return
     */
    @Override
    public boolean deleteQuestion(Integer id,Integer userId,Integer creator, HttpServletRequest request) {
        if (request.getAttribute("admin_roles")!=null){
            boolean b = questionMapper.deleteQuestion(id);
            return true;
        }
        if (request.getAttribute("user_roles")!=null){
            // 获取claims中的ID和userId 匹配
            Claims user_roles = (Claims) request.getAttribute("user_roles");
            String id1 = user_roles.getId();
            if (id1.equals(userId.toString())) {
                boolean b = questionMapper.deleteQuestion(id);
                return true;
            }else{
                // 权限不足
                throw new CustomizeException(CustomizeErrorCode.PERMISSION_DENIED);
            }
        }
        // 权限不足
        throw new CustomizeException(CustomizeErrorCode.PERMISSION_DENIED);
    }


}
