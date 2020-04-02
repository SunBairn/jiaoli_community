package com.zls.controller;

import com.zls.pojo.Question;
import com.zls.service.QuestionCommentService;
import com.zls.service.QuestionService;
import entity.Page;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.apache.ibatis.annotations.Arg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.GetHostIp;
import utils.PageUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionCommentService questionCommentService;

    /**
     * 添加问题或贴子
     * @param question  问题（帖子实体）
     * @return
     */
    @PostMapping("/add/{type}")
    public Result addQuestion(@RequestBody Question question){
        boolean b = questionService.addQuestion(question);
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "添加问题失败！");
    }

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
        if (pageList == null) {
            return new Result(false, StatusCode.NO_DATA_EXIST, "目标数据不存在");
        }
        PageResult pageResult = PageUtils.packagingPageResult(pageList);
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 根据ID查询问题或帖子
     * @param id  questionId
     * @return
     */
    @GetMapping("/{id}")
    public Result findQuestionWithUserWithCommentById(@PathVariable("id") Integer id){
        Question question = questionService.findQuestionWithUserWithCommentById(id);
        if (question == null) {
            return new Result(false, StatusCode.NO_DATA_EXIST, "目标数据不存在");
        }
        Long commentCount = questionCommentService.getCountByParentId(id);
        Map<String, Object> map = new HashMap<>();
        map.put("question", question);
        map.put("commentCount", commentCount);
        return new Result(true, StatusCode.OK, "查询成功",map);
    }

    /**
     * 用户给问题点赞功能
     * @param questionId  questionID
     * @param liketor 点赞者
     * @return
     */
    @GetMapping("/like")
    public Result likeQuestion(@RequestParam("questionId") Integer questionId,@RequestParam("liketor") Integer liketor){
        boolean b = questionService.likeQuestion(questionId, liketor);
        if (b) {
            return new Result(true, StatusCode.OK, "点赞成功！");
        }
        return new Result(false, StatusCode.ERROR, "你已经点过赞了！");
    }
}
