package com.zls.controller;

import com.zls.pojo.ArticleComment;
import com.zls.service.ArticleCommentService;
import entity.Result;
import entity.StatusCode;
import org.apache.ibatis.annotations.Arg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
