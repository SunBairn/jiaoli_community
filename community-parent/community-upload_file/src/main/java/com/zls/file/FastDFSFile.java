package com.zls.file;

import lombok.Data;

import java.io.Serializable;
@Data
public class FastDFSFile implements Serializable {
    // 文件名字
    private String name;
    // 文件内容
    private byte[] content;
    // 文件类型（扩展名）
    private String ext;
    // 上传时间
    private String time;
    // 文件上传者
    private String author;

    public FastDFSFile(String name, byte[] content, String ext,String author,String time) {
        this.name = name;
        this.content = content;
        this.ext = ext;
        this.time = time;
        this.author = author;
    }

    public FastDFSFile(String name, byte[] content, String ext) {
        this.name = name;
        this.content = content;
        this.ext = ext;
    }
}
