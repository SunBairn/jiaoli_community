package com.zls.controller;

import com.zls.pojo.User;
import com.zls.service.UserService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import utils.JWTUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 用户登录功能
     * @param map
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String ,String> map,HttpServletRequest request){
        // 防止用户重新登录，在redis缓存中存入重复的token
        Cookie[] cookies = request.getCookies();
        String jiaoliToken1=null ;
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if ("jiaoli_token".equals(cookie.getName())) {
                    jiaoliToken1 = cookie.getValue();
                }
            }
        }
        Map<String, Object> map2 = userService.findByMobileAndPassword(map,jiaoliToken1);
        User user = (User) map2.get("user");
        String jiaoliToken = (String) map2.get("jiaoliToken");
        if (user!=null) {
            String token = JWTUtils.createJWT(user.getId() + "", user.getNickname(), null, "user");
            Map<String, String> map1 = new HashMap<>();
            map1.put("token", token);
            map1.put("userId", user.getId()+"");
            map1.put("nickname", user.getNickname());
            map1.put("avatar", user.getAvatar());
            // 用于自动登录存储在cookie中的值
            map1.put("jiaoliToken", jiaoliToken);
            return new Result(true, StatusCode.OK, "登录成功",map1);
        }else {
            return new Result(false, StatusCode.ERROR,"用户名或密码错误");
        }
    }


    /**
     * 用户自动登录功能
     * @param
     * @return
     */
    @GetMapping("/autoLogin")
    public Result autoLogin(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if ("jiaoli_token".equals(cookie.getName())) {
                String value = cookie.getValue();
                Map<String, Object> map = userService.autoLogin(value);
                if (map!=null){
                    User user = (User) map.get("user");
                    String jiaoliToken = (String) map.get("jiaoliToken");
                    // 颁发token
                    String token = JWTUtils.createJWT(user.getId() + "", user.getNickname(), null, "user");
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("token", token);
                    map1.put("userId", user.getId()+"");
                    map1.put("nickname", user.getNickname());
                    map1.put("avatar", user.getAvatar());
                    // 用于自动登录存储在cookie(jiaolitoken)中的值
                    map1.put("jiaoliToken", jiaoliToken);

                    return new Result(true, StatusCode.OK, "自动登录成功",map1);
                }
            }
        }
         return new Result(false, StatusCode.ERROR,"自动登录失败！");
    }

    /**
     * 退出登录
     * @param userId
     * @return
     */
    @GetMapping("/loginOut")
    public Result loginOut(@RequestParam("userId") Integer userId,@RequestParam("jiaoliToken") String jiaoliToken,
                           HttpServletRequest request,HttpServletResponse response){
        // 1、清除缓存中的用户信息
        userService.loginOut(userId, jiaoliToken);
        // 2、清除cookie中的信息
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("jiaoli_token".equals(cookie.getName()) || "jwttoken".equals(cookie.getName())) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        // 3、删除sessionStorage中的信息,在前台删除
        return new Result();
    }

    /**
     * 当token过期了刷新token
     * @param userId 用户ID
     * @param nickname 用户昵称
     * @return
     */
    @GetMapping("/reflashToken")
    public Result reflashToken(@RequestParam("userId")String userId,@RequestParam("nickname") String nickname){
        String token = JWTUtils.createJWT(userId, nickname, null, "user");
        if (token==null){
            return new Result(false, StatusCode.ERROR, "刷新token失败");
        }
        return new Result(true, StatusCode.OK, "刷新token成功", token);
    }


}
