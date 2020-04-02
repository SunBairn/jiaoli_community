package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 文章评论实体
 */
@Data
public class ArticleComment implements Serializable {
    // 评论ID
    private Integer id ;
    private String content;
    private Integer articleId; // 文章ID
    private Integer userId;
    // 1代表评论，2代表回复
    private Integer type;
    // 父类ID
    private Integer parentId;
    // 发布时间
    private Long publishdate;
    private Integer likeCount;
    // 评论的回复数
    private Integer replyCount;
}
