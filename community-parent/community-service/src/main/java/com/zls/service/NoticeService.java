package com.zls.service;

import com.zls.pojo.Notice;
import entity.Page;

public interface NoticeService {

    Page<Notice> findAllNotice(int page);
}
