package com.zls.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Document(indexName = "index_article",type = "article")
public class ArticleSearch implements Serializable {
    @Id
    private Integer id;

   // @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word",type = FieldType.Text)
    private String title;

    private String gmt_modified;

    private String type;
}
