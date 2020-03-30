package com.zls.service.impl;

import com.zls.mapper.NoticeMapper;
import com.zls.pojo.Notice;
import com.zls.service.NoticeService;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    NoticeMapper noticeMapper;
    /**
     * 分页查询所有的公告
     * @param page
     * @return
     */
    @Override
    public Page<Notice> findAllNotice(int page) {
        Long total = noticeMapper.noticeCount();
        Page<Notice> page1 = new Page<>(total,page,Page.pageSize);
        List<Notice> allNotice = noticeMapper.findAllNotice(page1.getStart(), page1.getSize());
        page1.setList(allNotice);
        return page1;
    }
}
