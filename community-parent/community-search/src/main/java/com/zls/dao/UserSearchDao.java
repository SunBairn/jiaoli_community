package com.zls.dao;

import com.zls.pojo.ArticleSearch;
import com.zls.pojo.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;


@Component
public interface UserSearchDao extends ElasticsearchRepository<UserSearch,Integer> {

    Page<UserSearch> findByNickname(String keyword, Pageable pageable);

}
