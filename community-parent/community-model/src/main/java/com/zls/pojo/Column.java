package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 文章专栏实体
 */
@Data
public class Column implements Serializable {
    private Integer id;
    private String name;
    private String summary;
    private Integer adminId;
    private Long createTime;
    private Long updateTime;
    private Integer state;
}
