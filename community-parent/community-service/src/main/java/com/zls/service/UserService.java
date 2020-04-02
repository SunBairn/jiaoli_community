package com.zls.service;

import com.zls.pojo.User;

import javax.servlet.http.HttpServletResponse;
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

}
