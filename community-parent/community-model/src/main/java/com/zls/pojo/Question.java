package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 问题或帖子实体
 */
@Data
public class Question implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer likeCount;
    private Integer type;
    private Integer commentCount;
    private User user; // 一个问题对应一个用户
    private List<QuestionComment> questionCommentList;  // 一个问题对应多个评论
}
