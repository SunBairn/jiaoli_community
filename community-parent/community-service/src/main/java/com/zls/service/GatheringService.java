package com.zls.service;

import com.zls.pojo.Gathering;
import entity.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface GatheringService {

    /**
     * 分页查询所有的活动，并按最新活动进行排序
     * @param page
     * @return
     */
    Page<Gathering> findAllGathering(int page);


    /**
     * 根据ID查询活动信息
     * @param id
     * @return
     */
    Gathering findGatheringById(@Param("id") Integer id);
}
