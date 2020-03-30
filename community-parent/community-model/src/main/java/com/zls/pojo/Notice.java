package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告实体
 */
@Data
public class Notice implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private Long createTime;
    private Long modifiedTime;
    private String url;
    private String name;

}
