package com.zls.service;

import com.zls.file.FastDFSFile;
import com.zls.utils.FastDFSUtil;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.csource.common.MyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileService {

    /**
     * 文件上传
     * @param file  文件类型
     * @param username 上传者的用户名
     * @return
     * @throws IOException
     */
    public String uploadFile(MultipartFile file,String username) throws IOException {
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(currentTime);
        FastDFSFile fastDFSFile = new FastDFSFile(file.getOriginalFilename(), file.getBytes(), StringUtils.getFilenameExtension(file.getOriginalFilename()),
                username, date);
        String trackerUrl = FastDFSUtil.getTrackerUrl();
        String[] upload = null;
        try {
              upload = FastDFSUtil.upload(fastDFSFile);

        } catch (MyException e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAILED);
        }
        String url = trackerUrl + "/" + upload[0] + "/" + upload[1];
        return url;
    }

    /**
     * 删除文件
     * @param groupName 文件所在组名
     * @param remoteFileName 文件名
     * @return
     */
    public void deleteFile(String groupName, String remoteFileName) {
        try {
             FastDFSUtil.deleteFile(groupName, remoteFileName);
        } catch (Exception e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_DELETE_FAILED);
        }
    }
}
