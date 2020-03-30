package com.zls.mapper;

import com.zls.pojo.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository public interface NoticeMapper {

    /**
     * 分页查询所有公告信息，默认按时间降序
     * @param start
     * @param size
     * @return
     */
    @Select("select * from tb_notice order by create_time desc limit #{start},#{size}")
    List<Notice> findAllNotice(@Param("start") Long start,@Param("size") int size);


    /**
     * 查询总公告书
     * @return
     */
    @Select("select count(1) from tb_notice")
    Long noticeCount();
}
