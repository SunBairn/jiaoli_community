package com.zls.service;

import com.zls.pojo.Gathering;
import entity.Page;


public interface GatheringService {

    /**
     * 分页查询所有的活动，并按最新活动进行排序
     * @param page
     * @return
     */
    Page<Gathering> findAllGathering(int page);
}
