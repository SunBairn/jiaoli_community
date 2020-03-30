package com.zls.mapper;

import com.zls.pojo.Gathering;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository public interface GatheringMapper {


    /**
     * 分页查询所有的活动，并按最新活动进行排序
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_gathering order by  start_time desc limit #{start},#{size}")
    List<Gathering> findAllGathering(@Param("start") Long start,@Param("size") int size);


    @Select("select count(1) from tb_gathering")
    Long gatheringCount();
}
