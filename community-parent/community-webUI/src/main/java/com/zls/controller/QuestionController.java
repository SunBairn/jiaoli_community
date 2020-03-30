package com.zls.controller;

import com.zls.pojo.Question;
import com.zls.service.QuestionService;
import entity.Page;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.GetHostIp;
import utils.PageUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    /**
     * 分页查询问题或者帖子
     * @param type
     * @param page
     * @return
     */
    @GetMapping("/find/{type}")
    public Result findAllQuestion(@PathVariable("type") Integer type,
                                  @RequestParam(name = "page",defaultValue = "1") int page,
                                  @RequestParam(name="sort",defaultValue = "new") String sort, HttpServletResponse response){
        Page<Question> pageList = questionService.findAllQuestion(type,page,sort);
        PageResult pageResult = PageUtils.packagingPageResult(pageList);
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    @GetMapping("/{id}")
    public Result findQuestionWithUserWithCommentById(@PathVariable("id") Integer id){
        Question question = questionService.findQuestionWithUserWithCommentById(id);
        return new Result(true, StatusCode.OK, "查询成功",question);
    }
}
