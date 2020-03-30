package com.zls.controller;

import com.zls.pojo.Gathering;
import com.zls.service.GatheringService;
import entity.Page;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.PageUtils;

@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/gathering")
public class GatheringController {
    @Autowired
    GatheringService gatheringService;

    @GetMapping("/find")
    public Result findAllGathering(@RequestParam(name = "page",defaultValue = "1") int page){
        Page<Gathering> allGathering = gatheringService.findAllGathering(page);
        PageResult pageResult = PageUtils.packagingPageResult(allGathering);
        return new Result(true,20000,"查询成功",pageResult);
    }

}
