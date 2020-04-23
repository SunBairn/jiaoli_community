package com.zls.mapper;

import com.zls.pojo.Collection;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 收藏dao
 */
@Mapper
@Repository public interface CollectionMapper {

    /**
     * 判读用户是否收藏了某篇文章
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return
     */
    /**
     * 查看收藏
     * @param userId
     * @param articleId
     * @return
     */
    @Select("select count(*) from tb_collection where user_id=#{userId}  and article_id=#{articleId} ")
    int findCollectionByUserIdAndArticleId(@Param("userId") Integer userId,@Param("articleId") Integer articleId);


    /**
     * 添加收藏
     * @param collection  收藏实体
     * @return
     */
    @Insert("insert into tb_collection (user_id, article_id) value (#{userId} ,#{articleId} )")
    int addCollection(Collection collection);


    /**
     * 取消收藏
     * @param userId
     * @param articleId
     * @return
     */
    @Delete("delete from tb_collection where user_id=#{userId}  and article_id=#{articleId} ")
    int deleteCollection(@Param("userId") Integer userId,@Param("articleId") Integer articleId);

    /**
     * 根据用户ID查询收藏数
     * @param userId 用户ID
     * @return
     */
    @Select("select count(*) from tb_collection where user_id=#{userId} ")
    Integer findCollectionCountByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户的ID查询收藏的文章ID
     * @param userId 用户ID
     */
    Set<Integer> findArticleId(@Param("userId") Integer userId);
}
