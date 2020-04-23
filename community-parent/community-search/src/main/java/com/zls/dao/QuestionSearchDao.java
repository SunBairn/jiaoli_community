package com.zls.dao;

import com.zls.pojo.ArticleSearch;
import com.zls.pojo.QuestionSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;


@Component
public interface QuestionSearchDao extends ElasticsearchRepository<QuestionSearch,Integer> {

    Page<QuestionSearch> findByTitle(String keyword, Pageable pageable);

}
