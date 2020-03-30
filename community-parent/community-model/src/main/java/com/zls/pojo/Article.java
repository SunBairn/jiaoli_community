package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 文章实体
 */
@Data
public class Article implements Serializable {


    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    // 文章上传文件的URL地址
    private String url;
    // 文章上传图片的URL地址
    private String imageUrl;
    // 文章标签
    private String tag;

    private User user;

}
