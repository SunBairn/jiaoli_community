package com.zls.service;

import com.zls.pojo.Article;
import entity.Page;

public interface ArticleService {

    Page<Article> findAllArticle(int page,String sort);


}
