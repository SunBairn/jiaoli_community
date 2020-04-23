package com.zls.service.impl;

import com.zls.mapper.AttentionMapper;
import com.zls.mapper.UserMapper;
import com.zls.pojo.Attention;
import com.zls.service.AttentionService;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AttentionServiceImpl implements AttentionService {

    @Autowired
    AttentionMapper attentionMapper;

    @Autowired
    UserMapper userMapper;
    /**
     * 添加关注
     * @param userId
     * @param targetuserId
     * @return
     */
    @Override
    public boolean addAttention(Integer userId, Integer targetuserId) {
        int i = attentionMapper.addAttention(userId, targetuserId);
        // 关注成功后自己的关注数+1，被关注用户的粉丝数+1
        int i1 = userMapper.incrFansCount(targetuserId);
        int i2 = userMapper.incrFollowCount(userId);
        if (i==0 || i1==0 || i2==0){
            throw new CustomizeException(CustomizeErrorCode.TRANSACTION_ROLLBACK);
        }
        return true;
    }

    /**
     * 取消关注
     * @param userId
     * @param targetuserId
     * @return
     */
    @Override
    public boolean deleteAttention(Integer userId, Integer targetuserId) {
        int i = attentionMapper.deleteAttention(userId, targetuserId);
        // 取消关注后自己的关注数-1，被关注用户的粉丝数-1
        int i1 = userMapper.decrFansCount(targetuserId);
        int i2 = userMapper.decrFollowCount(userId);
        if (i == 0 || i1==0 || i2==0) {
            throw new CustomizeException(CustomizeErrorCode.TRANSACTION_ROLLBACK);
        }
        return true;
    }

    /**
     * 查看某个用户是否关注了另一个用户
     * @param userId
     * @param targetuserId
     * @return
     */
    @Override
    public Attention selectAttention(Integer userId, Integer targetuserId) {
        Attention attention = attentionMapper.selectAttention(userId, targetuserId);
        if (attention != null) {
            return attention;
        }
        return null;
    }

    /**
     * 根据用户ID查询所有的关注列表
     * @param userId
     * @return
     */
    @Override
    public List<Integer> findAttentionByUserId(Integer userId) {
        List<Integer> list = attentionMapper.findAttentionByUserId(userId);
        if (list != null) {
            return list;
        }
        return null;
    }
}
