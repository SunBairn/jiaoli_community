package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * 招聘信息实体
 */
@Data
public class Recruit implements Serializable {
    private Integer id;
    private String jobname;
    private String salary;
    private String condition; // 经验要求
    private String education;
    private String address;
    private String eid;   // 企业ID
    private Long gmtCreatetime;
    private String content1;
    private String content2;

}
