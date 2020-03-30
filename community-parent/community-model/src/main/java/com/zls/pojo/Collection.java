package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 收藏文章实体
 */
@Data
public class Collection implements Serializable {
    private Integer userId;
    private Integer articleId;
}
