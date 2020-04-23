package com.zls.controller;

import com.zls.pojo.Article;
import com.zls.pojo.Column;
import com.zls.pojo.Question;
import com.zls.pojo.Tag;
import com.zls.service.ArticleService;
import entity.Page;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.PageUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;


    /**
     * 添加文章
     * @param article  文章实体
     * @return
     */
    @PostMapping("/add")
    public Result addQuestion(@RequestBody Article article){
        boolean b = articleService.addArticle(article);
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "添加问题失败！");
    }


    /**
     * 修改文章
     * @param article  文章实体
     * @return
     */
    @PutMapping("/update")
    public Result updateArticle(@RequestBody Article article) {
        boolean b = articleService.updateArticle(article);
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "文章修改失败！");
    }

    /**
     * 分页查询所有文章，以及排序
     * @param page
     * @param sort
     * @return
     */
    @GetMapping("/find")
    public Result findAllArticle(@RequestParam(name = "page",defaultValue = "1") int page
            ,@RequestParam(name = "sort",defaultValue = "new") String sort){
        Page<Article> allArticle = articleService.findAllArticle(page, sort);
        if (allArticle == null) {
            return new Result(false, StatusCode.NO_DATA_EXIST, "目标数据不存在！");
        }
        PageResult pageResult = PageUtils.packagingPageResult(allArticle);
        return  new Result(true, StatusCode.OK,"查询成功",pageResult);
    }


    /**
     * 用户给文章点赞功能
     * @param articleId  articleId
     * @param liketor 点赞者
     * @return
     */
    @GetMapping("/like")
    public Result likeArticle(@RequestParam("articleId") Integer articleId,@RequestParam("liketor") Integer liketor){
        boolean b = articleService.likeArticle(articleId, liketor);
        if (b) {
            return new Result(true, StatusCode.OK, "点赞成功！");
        }
        return new Result(false, StatusCode.ERROR, "你已经点过赞了！");
    }

    /**
     * 根据ID查询某篇文章以及用户信息
     * @param id
     * @return
     */
    @GetMapping("/find/{id}")
    public Result findArticleById(@PathVariable("id") Integer id){
        Article article = articleService.findArticleWithUserById(id);
        if (article!=null){
            return new Result(true,StatusCode.OK,"查询成功！",article);
        }
        return new Result(false,StatusCode.ERROR,"查询文章失败！");
    }

    /**
     * 查询state为 1 的标签
     * @return
     */
    @GetMapping("/find/tag")
    public Result findTagByState(){
        List<Tag> tags = articleService.findTagByState();
        if (tags!=null){
            return new Result(true, StatusCode.OK, "查询成功！", tags);
        }
        return new Result(false, StatusCode.ERROR, "查询标签失败！");
    }


    /**
     * 查询state为 1 的专栏
     * @return
     */
    @GetMapping("/find/column")
    public Result findColumnByState(){
        List<Column> columns = articleService.findColumnBystate();
        if (columns != null) {
            return new Result(true, StatusCode.OK, "查询成功！", columns);
        }
        return new Result(true, StatusCode.OK, "查询专栏失败！");
    }


    /**
     * 根据UserID查询文章
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/find/all/{userId}")
    public Result findArticonByUserId(@PathVariable("userId") Integer userId) {
        List<Article> articleByUserId = articleService.findArticleByUserId(userId);
        return new Result(true, StatusCode.OK, "查询文章成功", articleByUserId);
    }


    /**
     * 根据ID删除文章
     * @param id 文章ID
     * @param userId 文章作者ID
     * @param request
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteArticle(@RequestParam("id") Integer id, @RequestParam("userId") Integer userId, HttpServletRequest request){
        boolean b = articleService.deleteArticle(id, userId, request);
        if (b) {
            return new Result();
        }
        return new Result(false,StatusCode.ERROR,"删除失败");
    }

    /**
     * 根据用户ID和专栏ID查询文章
     * @param userId
     * @param columnId
     * @return
     */
    @GetMapping("/find/userId/columnId")
    public Result findArticleByUserIdAndColumnId(@RequestParam("userId") Integer userId,
                                                        @RequestParam("columnId") Integer columnId) {
        List<Article> articles = articleService.findArticleByUserIdAndColumnId(userId, columnId);
        return new Result(true,StatusCode.OK,"执行成功！",articles);
    }

    /**
     * 查询热门文章
     * @return
     */
    @GetMapping("/find/hot")
    public Result findHotArticle(){
        List<Article> hotArticle = articleService.findHotArticle();
        return new Result(true, StatusCode.OK, "查询成功！", hotArticle);
    }

}
