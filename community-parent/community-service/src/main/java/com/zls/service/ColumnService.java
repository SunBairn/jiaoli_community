package com.zls.service;

import com.zls.pojo.Column;

import java.util.List;
import java.util.Map;

/**
 * 专栏服务接口
 */
public interface ColumnService {

    /**
     * 查询公开专栏的名称和每个用户对应的文章数量
     * @param userId 这个用户ID是用于查询数量的
     * @return
     */
    Map<String, List> findColumnName(Integer userId);

    /**
     * 根据ID查询专栏名称和ID
     * @param id
     */
    Column findColumnNameById(Integer id);
}
