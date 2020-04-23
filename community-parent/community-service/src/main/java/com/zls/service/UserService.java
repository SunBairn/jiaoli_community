package com.zls.service;

import com.zls.pojo.Article;
import com.zls.pojo.User;
import com.zls.vo.PageHome;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * 发送短信验证码
     * @param mobile
     */
    void sendSms(String mobile);


    /**
     * 校验用户是否被注册
     * @param mobile
     * @return boolean
     */
    boolean checkUser(String mobile);




    /**
     * 注册用户
     * @param user
     * @param code
     */
    void addUser(User user,String code);


    /**
     * 用户登录,根据用户信息查询用户是否存在(登录)
     * @param map
     * @return
     */
    Map<String,Object> findByMobileAndPassword(Map<String,String> map,String token);

    /**
     * 用户自动登录,如果可以则直接根据用户的ID查询用户的信息
     * @param jiaoliToken
     * @return
     */
    Map<String,Object> autoLogin(String jiaoliToken);

    /**
     * 退出登录
     * @param userId  用户ID
     * @return
     */
    boolean loginOut(Integer userId,String jiaoliToken);


    /**
     * 根据用户ID去查询用户信息
     * @param id
     * @return
     */
    User findById(Integer id);

    /**
     * 根据用户的ID去删除某个用户已
     * @param id
     */
    void deleteUser(Integer id);

    /**
     * 修改用户信息
     * @param user 用户实体
     * @return
     */
    boolean updateUser(User user);

    /**
     * 修改用户头像
     * @param id userId
     * @param avatar 用户头像
     * @return
     */
    boolean updateUserAvatar(Integer id,String avatar);

    /**
     * 根据用户ID获取用户的信息和用户主页需要显示的信息
     * @param id userId
     */
    PageHome findUserAndOtherById(Integer id);

    /**
     * 根据用户ID查询收藏的文章
     * @param userId
     * @return
     */
    List<Article> findCollectionArticleByUserId(Integer userId);


    /**
     * 根据ID查询用户关注列表
     * @param id 用户ID
     * @return
     */
    List<User> findFollowListById(Integer id);


    /**
     * 根据ID查询用户的粉丝列表
     * @param id 用户ID
     * @return
     */
    List<User> findFansListById(Integer id);

    /**
     * 查询粉丝数排名前十的用户信息
     * @return
     */
    List<User> findHotUser();
}
