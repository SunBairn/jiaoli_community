package com.zls.controller;

import com.zls.pojo.Collection;
import com.zls.service.CollectionService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏controller层
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    CollectionService collectionService;

    /**
     * 判断用户是否已经收藏了该片文章
     * @param userId
     * @param articleId
     * @return
     */
    @GetMapping("/find")
    public Result findCollectionByUserIdAndArticleId(@RequestParam("userId") Integer userId,@RequestParam("articleId") Integer articleId) {
        boolean flag = collectionService.findCollectionByUserIdAndArticleId(userId,articleId);
        if (flag) {
            return new Result(true, StatusCode.OK, "用户已收藏！");
        }
        return new Result(false, StatusCode.OK, "未收藏！");
    }

    /**
     * 添加收藏
     * @param collection
     * @return
     */
    @PostMapping("/add")
    public Result addCollection(@RequestBody Collection collection) {
        boolean b = collectionService.addCollection(collection);
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "收藏失败！");
    }

    /**
     * 取消收藏
     * @param userId
     * @param articleId
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteCollection(@RequestParam("userId") Integer userId,@RequestParam("articleId") Integer articleId) {
        boolean b = collectionService.deleteCollection(userId,articleId);
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "取消收藏失败！");
    }
}
