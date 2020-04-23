import com.zls.SearchApplication;
import com.zls.dao.ArticleSearchDao;
import com.zls.dao.UserSearchDao;
import com.zls.pojo.ArticleSearch;
import com.zls.pojo.UserSearch;
import com.zls.service.SearchServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import org.springframework.test.context.junit4.SpringRunner;





@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class TestDemo {
    @Autowired
    ArticleSearchDao articleSearchDao;
    @Autowired
    UserSearchDao userSearchDao;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    SearchServiceImpl searchService;
    @Test
    public void test1(){
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().
//                withIndices("index_question", "index_article").
//        withQuery(matchQuery("title","16")).build();
//        List<QuestionSearch> articleSearches = elasticsearchTemplate.queryForList( searchQuery, QuestionSearch.class);
//        System.out.println(articleSearches.toString());
//        Optional<ArticleSearch> byId = searchDao.findById(14);
//        System.out.println(byId);
//        Optional<ArticleSearch> articleSearch = searchDao.findByTitle("西游");
//        System.out.println(articleSearch);
  //      Pageable pageRequest = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC,"id"));
////        SearchQuery queryBuilder = new NativeSearchQueryBuilder().withPageable(pageRequest).withQuery(queryStringQuery("文章485")).build();
////        AggregatedPage<ArticleSearch> articleSearches = elasticsearchTemplate.queryForPage(queryBuilder, ArticleSearch.class);
//        Page<ArticleSearch> byTitle = searchDao.findByTitle("文4", pageRequest);
//        System.out.println(byTitle);
//        Page<UserSearch> byNickname = userSearchDao.findByNickname("Java", pageRequest);
//        System.out.println(byNickname.getContent());
        searchService.findAllByKeyword("文章14", 0, 10);

    }
}
