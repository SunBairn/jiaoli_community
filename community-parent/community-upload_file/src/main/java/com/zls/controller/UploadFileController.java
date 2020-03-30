package com.zls.controller;

import ch.qos.logback.core.status.StatusUtil;
import com.zls.file.FastDFSFile;
import com.zls.utils.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import org.csource.common.MyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/upload")
public class UploadFileController {

    @PostMapping
    public Result uploadFile(@RequestParam("file") MultipartFile file) throws IOException, MyException {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = formatter.format(currentTime);
        // 封装文件信息
        FastDFSFile fastDFSFile = new FastDFSFile(file.getOriginalFilename(), // 文件名
                file.getBytes(),        // 文件内容
                StringUtils.getFilenameExtension(file.getOriginalFilename()),
                "水哥",data);// 文件扩展名

        String[] upload = FastDFSUtil.upload(fastDFSFile);
        String url =  "http://192.168.155.129:8080/"+upload[0]+"/"+upload[1];
        return new Result(true, StatusCode.OK, "文件上传成功！",url);
    }

}
