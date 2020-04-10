package com.zls.mapper;

import com.zls.pojo.Column;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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

}
