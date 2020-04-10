package com.zls.controller;

import com.zls.pojo.ArticleComment;
import com.zls.pojo.QuestionComment;
import com.zls.service.ArticleCommentService;
import entity.Result;
import entity.StatusCode;
import org.apache.ibatis.annotations.Arg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RequestMapping("/article/comment")
public class ArticleCommentController {

    @Autowired
    ArticleCommentService articleCommentService;

    /**
     * 用户给文章的评论点赞
     * @param commentId  commentId
     * @param liketor 点赞者
     * @return
     */
    @GetMapping("/like")
    public Result likeQuestionComment(@RequestParam("commentId") Integer commentId, @RequestParam("liketor") Integer liketor){
        boolean b = articleCommentService.likeComment(commentId, liketor);
        if (b) {
            return new Result(true, StatusCode.OK, "点赞成功！");
        }
        return new Result(false, StatusCode.ERROR, "你已经点过赞了！");
    }


    /**
     * 添加文章评论
     * @param articleComment 文章评论
     * @return
     */
    @PostMapping("/add")
    public Result addCOmment(@RequestBody ArticleComment articleComment) {
        boolean b = articleCommentService.addComment(articleComment);
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "评论失败！");
    }


    /**
     * 根据问题的ID分页查询一级评论（一页10个）
     * @param page 当前页（0页开始）
     * @param parentId  评论的父类ID
     * @param type 类型（是一级评论还是二级评论）
     * @return
     */
    @GetMapping("/find/{page}")
    public Result pageFind(@PathVariable("page")Integer page,@RequestParam("parentId") Integer parentId,
                           @RequestParam("type") Integer type){
        List<ArticleComment> articleComments = articleCommentService.pageFindCommentWithUserByParentId(parentId,type, page);
        if (articleComments==null){
            return new Result(false, StatusCode.ERROR, "查询的评论不存在！");
        }
        return new Result(true, StatusCode.OK, "查询成功", articleComments);
    }

    /**
     * 根据评论ID查询回复
     * @param parentId
     * @return
     */
    @GetMapping("/find/reply")
    public Result findAllReplyByParentId(@RequestParam("parentId") Integer parentId){
        List<ArticleComment> reply = articleCommentService.findAllReplyByParentId(parentId);
        if (reply != null) {
            return new Result(true, StatusCode.OK, "查询回复成功！", reply);
        }
        return new Result(false, StatusCode.ERROR, "查询回复失败");
    }

}
