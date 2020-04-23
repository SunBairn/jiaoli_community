package com.zls.service;

import com.zls.pojo.ArticleSearch;
import com.zls.pojo.QuestionSearch;
import com.zls.pojo.UserSearch;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class SearchServiceImpl {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 分页查询搜索到的所有内容,并且高亮显示
     * @return
     */
    public Map<String, List> findAllByKeyword(String keyword,Integer page,Integer size){
        if (keyword.isEmpty()){return null;}
        Pageable pageable = PageRequest.of(page, size);
        String preTag = "<font color='#dd4b39'>";//google的色值
        String postTag = "</font>";
        // 1、查询问题并高亮显示
        SearchQuery searchQuery1 = new NativeSearchQueryBuilder().withQuery(matchQuery("title", keyword))
                .withPageable(pageable)
                .withHighlightFields(new HighlightBuilder.Field("title").preTags(preTag)
                        .postTags(postTag))
                .build();
        AggregatedPage<QuestionSearch> questions = elasticsearchTemplate.queryForPage(searchQuery1, QuestionSearch.class,
                new SearchResultMapper() {
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        List<QuestionSearch> questionSearchesList = new ArrayList<>();
                        SearchHits hits = searchResponse.getHits();
                        for (SearchHit searchHit : hits) {
                            if (hits.getHits().length <= 0) {
                                return null;
                            }
                            QuestionSearch questionSearch = new QuestionSearch();
                            questionSearch.setId(Integer.valueOf(searchHit.getId()));
                            questionSearch.setType(searchHit.getType());
                            HighlightField title = searchHit.getHighlightFields().get("title");
                            if (title != null) {
                                questionSearch.setTitle(title.fragments()[0].toString());
                            }
                            questionSearchesList.add(questionSearch);
                        }
                        if (questionSearchesList.size()>0){
                            return new AggregatedPageImpl<>( (List<T>) questionSearchesList);
                        }
                        return null;
                    }
                });

        // 2、查询文章并且高亮显示
        SearchQuery searchQuery2 = new NativeSearchQueryBuilder().withQuery(matchQuery("title", keyword))
                .withPageable(pageable)
                .withHighlightFields(new HighlightBuilder.Field("title").preTags(preTag)
                        .postTags(postTag))
                .build();
        AggregatedPage<ArticleSearch> articles = elasticsearchTemplate.queryForPage(searchQuery2, ArticleSearch.class
                , new SearchResultMapper() {
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        List<ArticleSearch> articleSearchList = new ArrayList<>();
                        SearchHits hits = searchResponse.getHits();
                        for (SearchHit searchHit : hits) {
                            if (hits.getHits().length <= 0) {
                                return null;
                            }
                            ArticleSearch articleSearch = new ArticleSearch();
                            articleSearch.setId(Integer.valueOf(searchHit.getId()));
                            articleSearch.setType(searchHit.getType());
                            HighlightField title = searchHit.getHighlightFields().get("title");
                          //  articleSearch.setId();
                            if (title != null) {
                                articleSearch.setTitle(title.fragments()[0].toString());
                            }
                            articleSearchList.add(articleSearch);
                        }
                        if (articleSearchList.size() > 0) {
                            return new AggregatedPageImpl<>((List<T>) articleSearchList);
                        }
                        return null;
                    }
                });
        // 3、查询用户并且高亮显示
        SearchQuery searchQuery3 = new NativeSearchQueryBuilder().withQuery(matchQuery("nickname", keyword))
                .withPageable(pageable)
                .withHighlightFields(new HighlightBuilder.Field("nickname").preTags(preTag)
                        .postTags(postTag))
                .build();
        AggregatedPage<UserSearch> users = elasticsearchTemplate.queryForPage(searchQuery3, UserSearch.class,
                new SearchResultMapper() {
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        List<UserSearch> userSearchesList = new ArrayList<>();
                        SearchHits hits = searchResponse.getHits();
                        for (SearchHit searchHit : hits) {
                            if (hits.getHits().length <= 0) {
                                return null;
                            }
                            UserSearch userSearch = new UserSearch();
                            userSearch.setId(Integer.valueOf(searchHit.getId()));
                            userSearch.setType(searchHit.getType());
                            HighlightField nickname = searchHit.getHighlightFields().get("nickname");
                            if (nickname != null) {
                                userSearch.setNickname(nickname.fragments()[0].toString());
                            }
                            userSearchesList.add(userSearch);
                        }
                        if (userSearchesList.size()>0){
                            return new AggregatedPageImpl<>( (List<T>) userSearchesList);
                        }
                        return null;
                    }
                });
        Map<String, List> map = new HashMap<>();
        if (questions != null) {
            List<QuestionSearch> content = questions.getContent();
            map.put("questions",content);
        }
        if (articles != null) {
            List<ArticleSearch> content = articles.getContent();
            map.put("articles",content);
        }
        if (users != null) {
            List<UserSearch> content = users.getContent();
            map.put("users",content);
        }

        if (map.size() > 0) {
            return map;
        }
        return null;

    }
}
