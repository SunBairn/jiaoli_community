package com.zls.controller;

import com.zls.pojo.QuestionComment;
import com.zls.service.QuestionCommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 帖子问题评论控制层
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RequestMapping("/comment")
public class QuestionCommentController {
    @Autowired
    QuestionCommentService questionCommentService;
    @GetMapping("/find/{page}")
    public Result pageFind(@PathVariable("page")Integer page,@RequestParam("parentId") Integer parentId,
                           @RequestParam("type") Integer type){
        List<QuestionComment> questionComments = questionCommentService.pageFindCommentWithUserByParentId(parentId,type, page);
        if (questionComments==null){
            return new Result(false, StatusCode.ERROR, "查询的评论不存在！");
        }
        return new Result(true, StatusCode.OK, "查询成功", questionComments);
    }

}
