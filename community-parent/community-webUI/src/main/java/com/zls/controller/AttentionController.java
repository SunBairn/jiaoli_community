package com.zls.controller;

import com.zls.pojo.Attention;
import com.zls.service.AttentionService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RequestMapping("/attention")
public class AttentionController {

    @Autowired
    AttentionService attentionService;

    /**
     * 添加关注
     * @param attention
     * @return
     */
    @PostMapping("/add")
    public Result addAttention(@RequestBody Attention attention) {
        if(attention==null){
            return new Result(false, StatusCode.ERROR, "缺少参数！");
        }
        boolean b = attentionService.addAttention(attention.getUserId(),attention.getTargetuserId());
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR,"关注失败！");
    }

    /**
     * 取消关注
     * @param userId
     * @param targetuserId
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteAttention(@RequestParam("userId") Integer userId, @RequestParam("targetuserId") Integer targetuserId) {
        boolean b = attentionService.deleteAttention(userId, targetuserId);
        if (b) {
            return new Result();
        }
        return  new Result(false, StatusCode.ERROR,"关注失败！");
    }

    /**
     * 查看某个用户是否关注了另一个用户
     * @param userId
     * @param targetuserId
     * @return
     */
    @GetMapping("/find/isfollow")
    public Result selectAttention(@RequestParam("userId") Integer userId,@RequestParam("targetuserId") Integer targetuserId) {
        Attention attention = attentionService.selectAttention(userId, targetuserId);
        // 如果返回false，则表示已经关注了，否者没有关注
        if (attention == null) {
            return new Result(true,StatusCode.OK,"没有关注！");
        }
        return new Result(false, StatusCode.ERROR, "已经关注！");
    }
}
