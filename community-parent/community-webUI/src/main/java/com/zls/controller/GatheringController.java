package com.zls.controller;

import com.zls.pojo.Gathering;
import com.zls.service.GatheringService;
import entity.Page;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.PageUtils;

@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/gathering")
public class GatheringController {
    @Autowired
    GatheringService gatheringService;

    /**
     * 分页查询活动
     * @param page
     * @return
     */
    @GetMapping("/find")
    public Result findAllGathering(@RequestParam(name = "page",defaultValue = "1") int page){
        Page<Gathering> allGathering = gatheringService.findAllGathering(page);
        if (allGathering == null) {
            return new Result(false, StatusCode.NO_DATA_EXIST, "目标数据不存在");
        }
        PageResult pageResult = PageUtils.packagingPageResult(allGathering);
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


    /**
     * 根据ID查询活动信息
     * @param id
     * @return
     */
    @GetMapping("/select/{id}")
    public Result findGatheringById(@PathVariable("id") Integer id) {
        Gathering gathering = gatheringService.findGatheringById(id);
        return new Result(true, StatusCode.OK, "查询成功！", gathering);
    }

}
