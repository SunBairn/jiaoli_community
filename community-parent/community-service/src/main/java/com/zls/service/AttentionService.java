package com.zls.service;

import com.zls.pojo.Attention;

import java.util.List;

/**
 * 用户关注service层
 */
public interface AttentionService {

    /**
     * 添加关注
     * @param userId
     * @param targetuserId
     * @return
     */
    boolean addAttention( Integer userId,  Integer targetuserId);

    /**
     * 取消关注
     * @param userId
     * @param targetuserId
     * @return
     */
    boolean deleteAttention( Integer userId,  Integer targetuserId);

    /**
     * 查看某个用户是否关注了另一个用户
     * @param userId
     * @param targetuserId
     * @return
     */
    Attention selectAttention( Integer userId, Integer targetuserId);

    /**
     * 根据用户ID查询所有的关注列表
     * @param userId
     * @return
     */
    List<Integer> findAttentionByUserId( Integer userId);
}
