package com.zls.service;

import com.zls.pojo.Collection;

/**
 * 收藏dao 接口
 */
public interface CollectionService {

    /**
     * 查看收藏
     * @param userId
     * @param articleId
     * @return
     */
    boolean findCollectionByUserIdAndArticleId(Integer userId,Integer articleId);



    /**
     * 添加收藏
     * @param collection  收藏实体
     * @return
     */
    boolean addCollection(Collection collection);


    /**
     * 取消收藏
     * @param userId
     * @param articleId
     * @return
     */
    boolean deleteCollection(Integer userId,Integer articleId);
}
