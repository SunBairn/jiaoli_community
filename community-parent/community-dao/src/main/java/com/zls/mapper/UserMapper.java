package com.zls.mapper;

import com.zls.pojo.Article;
import com.zls.pojo.User;
import com.zls.vo.PageHome;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 根据用户ID获取用户的信息和在用户的个人主页显示的需要的信息
     * @param id userId
     * @return
     */
    @Select("select id,nickname,name,avatar,followcount,fanscount from tb_user where id=#{id} ")
    @Results({
            @Result(column = "id", property = "id", id = true),
            @Result(column = "nickname",property = "nickname"),
            @Result(column = "name",property = "name"),
            @Result(column = "avatar",property = "avatar"),
            @Result(column = "followcount",property = "followcount"),
            @Result(column = "fanscount",property = "fanscount"),
            @Result(column = "fanscount",property = "fanscount"),
            @Result(column = "id",property = "articles",many = @Many(select = "com.zls.mapper.ArticleMapper.findArticleByUserId")),
            @Result(column = "id",property = "questionCount",one = @One(select = "com.zls.mapper.QuestionMapper.findQuestionCountByCreator")),
            @Result(column = "id",property = "invitationCount",one = @One(select = "com.zls.mapper.QuestionMapper.findInvitationCountByCreator")),
            @Result(column = "id",property = "collectionCount",one = @One(select = "com.zls.mapper.CollectionMapper.findCollectionCountByUserId")),
            @Result(column = "id",property = "columnCount",one = @One(select = "com.zls.mapper.ColumnMapper.findColumnCount")),

    })
    PageHome findUserAndOtherById(Integer id);


    /**
     * 根据用户ID查询该用户收藏的文章
     * @param userId  用户ID
     */
    List<Article> findCollectionArticleByUserId(@Param("userId") Integer userId);

    /**
     * 粉丝数+1
     * @param id
     * @return
     */
    @Update("update tb_user set fanscount=fanscount+1 where id=#{id} ")
    int incrFansCount(@Param("id") Integer id);

    /**
     * 关注数 +1
     * @param id
     * @return
     */
    @Update("update tb_user set followcount = followcount+1 where id=#{id};")
    int incrFollowCount(@Param("id") Integer id);

    /**
     * 粉丝数 -1
     * @param id
     * @return
     */
    @Update("update tb_user set fanscount=fanscount-1 where id=#{id} ")
    int decrFansCount(@Param("id") Integer id);

    /**
     * 关注数 -1
     * @param id
     * @return
     */
    @Update("update tb_user set followcount = followcount-1 where id=#{id};")
    int decrFollowCount(@Param("id") Integer id);

    /**
     * 根据ID查询用户关注列表
     * @param id 用户ID
     * @return
     */
    @Select("select id,avatar,nickname from tb_user where id in (select targetuser_id from tb_attention where user_id=#{id}  )")
    List<User> findFollowListById(@Param("id") Integer id);


    /**
     * 根据ID查询用户的粉丝列表
     * @param id 用户ID
     * @return
     */
    @Select("select id,avatar,nickname from tb_user where id in (select user_id from tb_attention where targetuser_id=#{id} )")
    List<User> findFansListById(@Param("id") Integer id);

    /**
     * 查询粉丝数排名前十的用户信息
     */
    @Select("select id,nickname,avatar from tb_user order by fanscount desc limit 0,16")
    List<User> findHotUser();

}
