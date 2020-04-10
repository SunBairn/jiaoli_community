package com.zls.controller;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.zls.pojo.Question;
import com.zls.service.QuestionCommentService;
import com.zls.service.QuestionService;
import entity.Page;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.GetHostIp;
import utils.PageUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
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
    public Result addQuestion(@RequestBody Question question,@PathVariable("type") Integer type){
        boolean b = questionService.addQuestion(question,type);
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "添加问题失败！");
    }

    /**
     * 修改问题或帖子
     * @param question 问题实体
     * @return
     */
    @PutMapping("/update")
    public Result updateQuestion(@RequestBody Question question) {
        boolean b = questionService.updateQuestion(question);
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "问题或帖子修改失败！");
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
        return new Result(true, StatusCode.OK, "查询成功",question);
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


    /**
     * 根据creator和type查询问题或帖子
     * @param creator 作者
     * @param type 类型
     * @return
     */
    @GetMapping("/find")
    public Result findQuestionByCreatorAndType(@RequestParam("creator") Integer creator, @RequestParam("type") Integer type) {
        List<Question> questions = questionService.findQuestionByCreatorAndType(creator, type);
        if (questions!=null){
            return new Result(true, StatusCode.OK, "查询成功", questions);
        }
        return new Result(false, StatusCode.ERROR, "查询失败！");
    }

    /**
     * 根据ID删除问题或帖子
     * @param id questionId
     * @param userId 用户ID
     * @param creator 问题作者
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteQuestion(@RequestParam("id") Integer id,@RequestParam("userId") Integer userId,
                                 @RequestParam("creator") Integer creator,HttpServletRequest request){
        boolean b = questionService.deleteQuestion(id, userId, creator, request);
        if (b) {
            return new Result();
        }
        return new Result(false,StatusCode.ERROR,"删除失败！");
    }
}
