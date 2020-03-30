package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 文章标签中间实体
 */
@Data
public class ArticleTag implements Serializable {
    private Integer articleId;
    private Integer tagId;
}
