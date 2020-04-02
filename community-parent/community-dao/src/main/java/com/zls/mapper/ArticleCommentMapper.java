package com.zls.mapper;

import com.zls.pojo.ArticleComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 文章评论mapper层
 */
@Mapper
@Repository public interface ArticleCommentMapper {


    /**
     * 添加文章评论
     * @param articleComment
     * @return
     */
    @Insert("insert into tb_article_comment (content, user_id, parent_id, publishdate, type, article_id) value (" +
            "#{articleComment.content} ,#{articleComment.userId} #{articleComment.parentId} ," +
            "#{articleComment.publishdate} ,#{articleComment.type},#{articleComment.articleId} )")
    boolean addComment(@Param("articleComment") ArticleComment articleComment);
}
