package com.zls.mapper;

import com.zls.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository public interface UserMapper {





    /**
     * 根据用户的ID去查询某个用户的所有信息
     * @param id
     * @return
     */
    @Select("select * from tb_user where id=#{id}")
     User findById(@Param("id") Integer id);


    /**
     * 根据用户的ID去查询某个用户的部分信息
     * @param id
     * @return
     */
    @Select("select id,nickname,avatar,fanscount from tb_user where id=#{id}")
    @Results(id = "userMapper",value = {
            @Result(column = "id",property = "id",id = true),
            @Result(column = "nickname",property = "nickname"),
            @Result(column = "avatar",property = "avatar"),
            @Result(column = "fanscount",property = "fanscount")
    })
    User findByIdPortion(@Param("id") Integer id);

    /**
     * 注册用户
     * @param user
     */
    @Insert("insert into tb_user (mobile,password,nickname,gmt_regdate,gmt_modified) value (#{user.mobile} ,#{user.password},#{user.nickname},#{user.gmtRegdate} ,#{user.gmtModified} )")
    void addUser(@Param("user") User user);


    /**
     * 根据号码和密码查询用户是否存在
     * @param mobile
     * @param password
     * @return
     */
    @Select("select * from tb_user where mobile=#{mobile} and password = #{password}")
    User findByMobileAndPassword(@Param("mobile")String mobile,@Param("password") String password);


    /**
     * 检查用户是否被注册过
     * @return
     */
    @Select("select * from tb_user where mobile=#{mobile}")
    User findByMobile(@Param("mobile") String mobile);


    /**
     * 根据用户的ID去删除某个用户
     * @param id
     */
    @Delete("delete from tb_user where id=#{id}")
    void deleteUser(@Param("id") Integer id);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    boolean updateUser(@Param("user") User user);

    /**
     * 修改头像
     * @param id userId
     * @param avatar 头像地址
     * @return
     */
    @Update("update tb_user set avatar=#{avatar} where id=#{id}")
    boolean updateUserAvatar(@Param("id") Integer id,@Param("avatar") String avatar);
}
