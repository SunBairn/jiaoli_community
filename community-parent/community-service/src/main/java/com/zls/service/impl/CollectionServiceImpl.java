package com.zls.service.impl;

import com.zls.mapper.CollectionMapper;
import com.zls.pojo.Collection;
import com.zls.service.CollectionService;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    CollectionMapper collectionMapper;
    /**
     * 判断用户是否已经收藏该片文章
     * @param userId
     * @param articleId
     * @return
     */
    @Override
    public boolean findCollectionByUserIdAndArticleId(Integer userId,Integer articleId) {
        int count = collectionMapper.findCollectionByUserIdAndArticleId(userId,articleId);
        if (count == 0) {
            // 如果为0 则没有收藏
            return false;
        }
        return true;
    }

    /**
     * 添加收藏
     * @param collection  收藏实体
     * @return
     */
    @Override
    public boolean addCollection(Collection collection) {
        // 判断该用户是否已经收藏了
        int count = collectionMapper.findCollectionByUserIdAndArticleId(collection.getUserId(), collection.getArticleId());
        if (count != 0) {
            throw new CustomizeException(CustomizeErrorCode.REPETITION_OPERATION);
        }
        int i = collectionMapper.addCollection(collection);
        if (i != 0) {
            return true;
        }
        return false;
    }

    /**
     * 取消收藏
     * @param userId
     * @param articleId
     * @return
     */
    @Override
    public boolean deleteCollection(Integer userId,Integer articleId) {
        int i = collectionMapper.deleteCollection(userId,articleId);
        if (i != 0) {
            return true;
        }
        return false;
    }
}
