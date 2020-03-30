package com.zls.pojo;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * 用户实体
 */
@Data
public class User implements Serializable {
    private Integer id;
    private String mobile;
    private String password;
    private String nickname;
    private String sex;
    private Date birthday;
    private String avatar;
    private String emai;
    private Long gmtRegdate;
    private Long gmtModified;
    private String interest;
    private Integer fanscount;
    private Integer followcount;
    private List<Question> questionList;

}
