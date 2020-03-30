package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员表
 */
@Data
public class Admin implements Serializable {

    // 管理员ID
    private Integer id;

    // 登录名
    private String loginname;

    // 密码
    private String password;

    // 状态
    private Integer state;

}
