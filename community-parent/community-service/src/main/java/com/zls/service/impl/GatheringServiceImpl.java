package com.zls.service.impl;

import com.zls.mapper.GatheringMapper;
import com.zls.pojo.Gathering;
import com.zls.service.GatheringService;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class GatheringServiceImpl implements GatheringService {
    @Autowired
    GatheringMapper gatheringMapper;
    @Override
    public Page<Gathering> findAllGathering(int page) {
        Long total = gatheringMapper.gatheringCount();
        Page<Gathering> page1 = new Page<>(total, page, Page.pageSize);
        List<Gathering> allGathering = gatheringMapper.findAllGathering(page1.getStart(),page1.getSize());
        page1.setList(allGathering);
        return page1;
    }

    /**
     * 根据ID查询活动信息
     * @param id
     * @return
     */
    @Override
    public Gathering findGatheringById(Integer id) {
        Gathering gathering = gatheringMapper.findGatheringById(id);
        return gathering;
    }
}
