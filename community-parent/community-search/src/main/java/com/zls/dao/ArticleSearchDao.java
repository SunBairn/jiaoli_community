package com.zls.dao;

import com.zls.pojo.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;


@Component
public interface ArticleSearchDao extends ElasticsearchRepository<ArticleSearch,Integer> {

    Page<ArticleSearch> findByTitle(String keyword, Pageable pageable);

}
