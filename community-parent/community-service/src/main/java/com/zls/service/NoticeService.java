package com.zls.service;

import com.zls.pojo.Notice;
import entity.Page;

public interface NoticeService {

    /**
     * 分页查询公告
     * @param page
     * @return
     */
    Page<Notice> findAllNotice(int page);

    /**
     * 根据ID查询公告信息
     * @param id
     * @return
     */
    Notice findNiticeById(Integer id);
}
