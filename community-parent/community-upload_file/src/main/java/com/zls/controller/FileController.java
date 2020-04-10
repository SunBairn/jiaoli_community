package com.zls.controller;

import com.zls.service.FileService;
import com.zls.utils.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws IOException
     * @throws MyException
     */
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) throws IOException, MyException {
        String url = fileService.uploadFile(file, username);
        if (url != null) {
            return new Result(true, StatusCode.OK, "文件上传成功！", url);
        }
        return new Result(false, StatusCode.ERROR, "文件上传失败！");
    }

    /**
     * 删除文件
     * @param fileUrl 文件的完整路径
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteFile(@RequestParam("fileUrl") String fileUrl) throws IOException {
        // 获取track的ip和端口地址
        String trackerUrl = FastDFSUtil.getTrackerUrl();
        String substring = fileUrl.substring(trackerUrl.length() + 1);
        int i = substring.indexOf("/");
        String groupName = substring.substring(0, i);
        String remoteFileName = substring.substring(groupName.length() + 1);
        fileService.deleteFile(groupName,remoteFileName);
        return new Result();
    }

}
