package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 标签实体
 */
@Data
public class Tag implements Serializable {
    private Integer id;
    private String tagname;
    private Integer state;
    private Integer count;
    private Integer type;

}
