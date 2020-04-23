package com.zls.controller;

import com.zls.pojo.Notice;
import com.zls.service.NoticeService;
import entity.Page;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.PageUtils;

@RestController
@RequestMapping("/notice")
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    /**
     * 分页查询所有公告，按时间降序
     * @param page
     * @return
     */
    @GetMapping("/find")
    public Result findAllNotice(@RequestParam("page") int page){
        Page<Notice> allNotice = noticeService.findAllNotice(page);
        if (allNotice == null) {
            return new Result(false, StatusCode.NO_DATA_EXIST, "目标数据不存在");
        }
        PageResult pageResult = PageUtils.packagingPageResult(allNotice);
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 根据ID查询公告
     * @param id
     * @return
     */
    @GetMapping("/select/{id}")
    public Result findNoticeById(@PathVariable("id") Integer id) {
        Notice niticeById = noticeService.findNiticeById(id);
        return new Result(true, StatusCode.OK, "查询成功", niticeById);
    }
}
