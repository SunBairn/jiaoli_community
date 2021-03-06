package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 问题或帖子评论实体
 */
@Data
public class QuestionComment implements Serializable {
    private Integer id;
    private String content;
    private Integer questionId; // 对应的问题ID
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Integer likeCount;
    private Integer replyCount;  // 评论的回复数
    private User user;  // 一个评论对应一个用户

}
