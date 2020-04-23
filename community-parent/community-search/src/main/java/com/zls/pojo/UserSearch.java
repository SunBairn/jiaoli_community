package com.zls.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Document(indexName = "index_user",type = "user")
public class UserSearch implements Serializable {
    @Id
    private Integer id;

   // @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word",type = FieldType.Text)
    private String nickname;

    private String gmt_modified;

    private String type;
}
