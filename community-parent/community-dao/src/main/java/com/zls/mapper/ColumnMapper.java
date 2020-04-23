package com.zls.mapper;

import com.zls.pojo.Column;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专栏dao
 */
@Mapper
@Repository public interface ColumnMapper {

    /**
     * 根据ID查询专栏的名称
     * @param id 专栏ID
     * @return
     */
    @Select("select id,name from tb_column where id=#{id} ")
    Column findNameById(@Param("id") Integer id);

    /**
     * 查询专栏的数量
     */
    @Select("select count(*) from tb_column where state=1")
    Integer findColumnCount();

    /**
     * 查询所有专栏的名称
     */
    @Select("select id,  name from tb_column where state=1")
    List<Column> findColumnName();

}
