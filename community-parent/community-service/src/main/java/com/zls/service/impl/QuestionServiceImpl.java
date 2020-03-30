package com.zls.service.impl;

import com.zls.mapper.QuestionMapper;
import com.zls.pojo.Question;
import com.zls.service.QuestionService;
import entity.Page;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Repository public class QuestionServiceImpl implements QuestionService {


    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
                String s = stringRedisTemplate.opsForValue().get("question:view_count:" + question.getId());
                if (s!=null) {
                    question.setViewCount(Integer.valueOf(s));
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
            }
        }
        if(sort.equals("await")){
            allQuestion=questionMapper.findAllQuestionWithUserByAwait(type,page1.getStart(),Page.pageSize);
            for(Question question:allQuestion){
                String s = stringRedisTemplate.opsForValue().get("question:view_count:" + question.getId());
                if (s!=null) {
                    question.setViewCount(Integer.valueOf(s));
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
     * 根据ID查询问题（或帖子），评论，用户并统计阅读数
     * @param id
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
        String v = stringRedisTemplate.opsForValue().get("question:view_count:" + id);
        // 将缓存中的阅读数返回给用户
        question.setViewCount(Integer.valueOf(v));
        return question;
    }


}
