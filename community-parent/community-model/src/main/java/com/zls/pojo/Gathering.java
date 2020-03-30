package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 校园活动实体
 */
@Data
public class Gathering implements Serializable {
    private Integer id;
    private String name;
    private String summary;
    private String detail;
    private String sponsor;
    private String image;
    private Long startTime;
    private Long endTime;
    private String address;

}
