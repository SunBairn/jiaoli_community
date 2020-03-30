package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;
@Data
public class Enterprise implements Serializable {
    private Integer id;
    private String name;

    // 企业简介
    private String summary;

    private String address;
    private String logo;
    private Integer jobcount;
    private String url;

}
