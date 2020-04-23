package com.zls.controller;

import com.zls.pojo.Column;
import com.zls.service.ColumnService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 专栏controller层
 */
@RestController
@RequestMapping("/column")
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
public class ColumnController {

    @Autowired
    ColumnService columnService;

    /**
     * 查询公开专栏的名称和每个用户对应的文章数量
     * @param userId
     * @return
     */
    @GetMapping("/find/name")
    public Result findColumnName(@RequestParam("userId") Integer userId){
        Map<String, List> map = columnService.findColumnName(userId);
        return new Result(true, StatusCode.OK, "执行成功！", map);
    }

    /**
     * 根据ID查询专栏名称
     * @param id
     * @return
     */
    @GetMapping("/find/name/id")
    public Result findColumnNameById(@RequestParam("id") Integer id){
        Column column = columnService.findColumnNameById(id);
        return new Result(true, StatusCode.OK, "执行成功！", column);
    }
}
