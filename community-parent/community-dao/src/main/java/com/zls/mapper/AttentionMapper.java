package com.zls.mapper;

import com.zls.pojo.Attention;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户关注mapper
 */
@Mapper
@Repository public interface AttentionMapper {
    /**
     * 添加关注
     * @param userId
     * @param targetuserId
     * @return
     */
    @Insert("insert into tb_attention(user_id, targetuser_id) value (#{userId} ,#{targetuserId} )")
    int addAttention(@Param("userId") Integer userId,@Param("targetuserId") Integer targetuserId);

    /**
     * 取消关注
     * @param userId
     * @param targetuserId
     * @return
     */
    @Delete("delete from tb_attention where user_id=#{userId} and targetuser_id=#{targetuserId} ")
    int deleteAttention(@Param("userId") Integer userId, @Param("targetuserId") Integer targetuserId);

    /**
     * 查看某个用户是否关注了另一个用户
     * @param userId
     * @param targetuserId
     * @return
     */
    @Select("select * from tb_attention  where user_id=#{userId} and targetuser_id=#{targetuserId}")
    Attention selectAttention(@Param("userId") Integer userId, @Param("targetuserId") Integer targetuserId);

    /**
     * 根据用户ID查询所有的关注列表
     * @param userId
     * @return
     */
    @Select("select targetuser_id from tb_attention where user_id=#{userId} ")
    List<Integer> findAttentionByUserId(@Param("userId") Integer userId);
}
