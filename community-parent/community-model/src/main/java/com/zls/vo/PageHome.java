package com.zls.vo;

import com.zls.pojo.Article;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 个人主页需要用到的数据
 */
@Data
public class PageHome implements Serializable {
    private Integer id;
    private String nickname;
    private String name;
    private String avatar;
    private Integer followcount;
    private Integer fanscount;
    private List<Article> articles;  //  文章
    private Integer questionCount;  //  问题数
    private Integer invitationCount;  //  帖子数
    private Integer collectionCount;  //  收藏数
    private Integer columnCount;  //  专栏数
}
