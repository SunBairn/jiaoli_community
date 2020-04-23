package com.zls.service.impl;

import com.zls.mapper.ArticleMapper;
import com.zls.pojo.Article;
import com.zls.pojo.Column;
import com.zls.pojo.Tag;
import com.zls.service.ArticleService;
import com.zls.utils.RedisServiceExtend;
import entity.Page;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.PageUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisServiceExtend redisServiceExtend;

    /**
     * 分页查询所有文章，以及排序
     * @param page 当前页
     * @param sort 排序规则
     * @return Page
     */

    @Override
    public Page<Article> findAllArticle(int page, String sort) {
        List<Article> articleList=null;
        Long total = articleMapper.articleCount();
        Page<Article> page1 = new Page<>(total, page, Page.pageSize);
        if(sort.equals("new")) {
            articleList = articleMapper.findAllArticleWithUser(page1.getStart(), page1.getSize());
        }
        if(sort.equals("hot")){
            articleList = articleMapper.findAllArticleWithUserByHot(page1.getStart(), page1.getSize());
        }
        page1.setList(articleList);
        return page1;
    }


    /**
     * 实现文章点赞功能
     * @param articleId  文章ID
     * @param liketor 点赞者ID
     */
    @Override
    public boolean likeArticle(Integer articleId, Integer liketor) {
        // 先从Redis中查找该用户有没有已经点赞了该文章
        Boolean flag = stringRedisTemplate.opsForValue().getBit("article:like_count:" + articleId, liketor);
        if (false){
            return false;
        }
        Boolean aBoolean = stringRedisTemplate.opsForValue().setBit("article:like_count:" + articleId, liketor, true);
        stringRedisTemplate.expire("article:like_count:" + articleId, 30, TimeUnit.DAYS);
        if (aBoolean){
            throw new CustomizeException(CustomizeErrorCode.LIKE_FAILED);
        }
        return true;
    }


    /**
     * 增加评论数+1
     * @param articleId 文章ID
     * @return
     */
    @Override
    public boolean incrementCommentCount(Integer articleId) {
        Boolean aBoolean = articleMapper.incrementCommentCount(articleId);
        if (aBoolean) {
            return true;
        }
        return false;
    }


    /**
     * 添加文章
     * @param article  文章实体
     * @return
     */
    @Override
    public boolean addArticle(Article article) {
        article.setGmtCreate(System.currentTimeMillis());
        article.setGmtModified(System.currentTimeMillis());
        boolean b = articleMapper.addArticle(article);
        if (b) {
            return true;
        }
        return false;
    }

    /**
     * 修改文章
     * @param article 文章实体
     * @return
     */
    @Override
    public boolean updateArticle(Article article) {
        article.setGmtModified(System.currentTimeMillis());
        boolean b = articleMapper.updateArticle(article);
        if (b){
            return true;
        }
        return false;
    }

    /**
     * 根据ID查询文章的信息展示在具体页面
     * @param id
     * @return
     */
    @Override
    public Article findArticleWithUserById(Integer id) {
        Article article = articleMapper.findArticleWithUserById(id);
        if (article==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        // 实现阅读数+1，采用Redis实现
        // 1、查询缓存中是否有阅读记录
        String s = stringRedisTemplate.opsForValue().get("article:view_count:" + id);
        if (StringUtils.isEmpty(s)) {
            // 如果缓存中没有，或者过期了，则从数据库中读取再添加到redis中
            Integer viewCount = articleMapper.getViewCount(id);
            stringRedisTemplate.opsForValue().set("article:view_count:"+id, String.valueOf(viewCount),20, TimeUnit.DAYS);
            stringRedisTemplate.opsForValue().increment("article:view_count:"+id);
        }else{
            stringRedisTemplate.opsForValue().increment("article:view_count:"+id);
        }
        // 避免出现EOF异常
        String v = stringRedisTemplate.boundValueOps("article:view_count:" + id).get(0,-1);
        // 将缓存中的阅读数返回给用户
        article.setViewCount(Integer.valueOf(v));
        // 将缓存中点赞数返回给用户
        Integer integer = redisServiceExtend.bitCount("article:like_count:" + id);
        if (integer!=null){
            article.setLikeCount(integer);
        }
        return article;
    }

    /**
     * 根据用户ID查询所有文章
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<Article> findArticleByUserId(Integer userId) {
        List<Article> articles = articleMapper.findArticleByUserId(userId);
        return articles;
    }

    /**
     * 根据文章ID删除文章
     * @param id 文章ID
     * @param userId 用户ID，作者
     * @param request
     * @return
     */
    @Override
    public boolean deleteArticle(Integer id, Integer userId, HttpServletRequest request) {
        // 1、如果是管理员则直接删除
        if (request.getAttribute("admin_roles") != null) {
            boolean b = articleMapper.deleteArticle(id);
            return b;
        }
        // 如果是用户，则判断用户的ID是否和article表的userID一致
        if (request.getAttribute("user_roles") != null) {
            Claims user_roles = (Claims) request.getAttribute("user_roles");
            if (user_roles.getId().equals(userId.toString())){
                boolean b = articleMapper.deleteArticle(id);
                return b;
            }else {
                return false;
            }
        }
        return false;
    }


    /**
     * 查询state为 1 的标签
     * @return
     */
    @Override
    public List<Tag> findTagByState() {
        List<Tag> tags = articleMapper.findTagByState();
        return tags;
    }


    /**
     * 查询state为 1 的专栏
     * @return
     */
    @Override
    public List<Column> findColumnBystate() {
        List<Column> columns = articleMapper.findColumnByState();
        return columns;
    }

    /**
     * 根据用户ID和专栏ID查询文章
     * @param userId
     * @param columnId
     * @return
     */
    @Override
    public List<Article> findArticleByUserIdAndColumnId(Integer userId, Integer columnId) {
        List<Article> articles = articleMapper.findArticleByUserIdAndColumnId(userId, columnId);
        return articles;
    }

    /**
     * 查询热门文章，并且放入缓存中
     * @return
     */
    @Override
    public List<Article> findHotArticle() {
        // 先从缓存中查，看缓存中有没有
        Set<String> keys = stringRedisTemplate.keys("hot:article:*");
        if (keys.size()!=0){
            List<Article> articles = new ArrayList<>();
            for (String key : keys) {
                String id = (String) stringRedisTemplate.opsForHash().get(key, "id");
                String title = (String) stringRedisTemplate.opsForHash().get(key, "title");
                String view_count = (String) stringRedisTemplate.opsForHash().get(key, "view_count");
                Article article = new Article();
                article.setId(Integer.valueOf(id));
                article.setTitle(title);
                article.setViewCount(Integer.valueOf(view_count));
                articles.add(article);
            }
            return articles;
        }
        // 否者从数据库中查询，在存入缓存中，过期时间为1天
        List<Article> hotArticle = articleMapper.findHotArticle();
        Map<String, String> map = new HashMap<>();
        for (Article article : hotArticle) {
            map.put("id", article.getId().toString());
            map.put("title", article.getTitle());
            map.put("view_count", article.getViewCount().toString());
            stringRedisTemplate.opsForHash().putAll("hot:article:"+article.getId(),map);
            stringRedisTemplate.expire("hot:article", 1, TimeUnit.DAYS);
        }

        return hotArticle;
    }
}
