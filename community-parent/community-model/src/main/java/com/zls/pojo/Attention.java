package com.zls.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户关注实体
 */
@Data
public class Attention implements Serializable {
    private Integer userId;
    private Integer targetuserId;
}
