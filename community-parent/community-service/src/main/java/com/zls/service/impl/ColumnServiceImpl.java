package com.zls.service.impl;

import com.zls.mapper.ArticleMapper;
import com.zls.mapper.ColumnMapper;
import com.zls.pojo.Column;
import com.zls.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专栏service层
 */
@Service
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    ColumnMapper columnMapper;
    @Autowired
    ArticleMapper articleMapper;
    /**
     * 查询公开专栏的名称和每个用户对应的文章数量
     * @return
     */
    @Override
    public Map<String, List> findColumnName(Integer userId) {
        List<Column> columnName = columnMapper.findColumnName();
        List<Map<Integer, Integer>> articleNums = articleMapper.findArticleByUserIdGroupByColumnId(userId);
        Map<String, List> map = new HashMap<>();
        map.put("columnName", columnName);
        map.put("articleNums", articleNums);
        return map;

    }

    /**
     * 根据ID查询专栏名称
     * @param id
     * @return
     */
    @Override
    public Column findColumnNameById(Integer id) {
        Column column = columnMapper.findNameById(id);
        return column;
    }
}
