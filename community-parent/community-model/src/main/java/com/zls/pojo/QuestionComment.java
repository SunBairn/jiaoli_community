package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 问题或帖子评论实体
 */
@Data
public class QuestionComment implements Serializable {
    private Long id;
    private String content;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long likeCount;
    private User user;  // 一个评论对应一个用户

}
