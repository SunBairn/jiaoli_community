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
import java.util.concurrent.TimeUnit;

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
        for (Cookie cookie : cookies) {
            if ("jiaoli_token".equals(cookie.getName())) {
                jiaoliToken1 = cookie.getValue();
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
            // 用于自动登录存储在token中的值
            map1.put("jiaoliToken", jiaoliToken);
            // 登录成功后将用户的id和昵称存到redis中，便于生成jwttoken时使用(过期时间为2天)
            redisTemplate.opsForHash().put("user:"+user.getId(),"id",user.getId());
            redisTemplate.opsForHash().put("user:"+user.getId(),"nickname",user.getNickname());
            redisTemplate.expire("user:" + user.getId(), 2, TimeUnit.DAYS);

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
                    // 用于自动登录存储在token中的值
                    map1.put("jiaoliToken", jiaoliToken);
                    // 自动登录成功后将用户的id和昵称存到redis中，便于生成jwttoken时使用(过期时间为2天)
                    redisTemplate.opsForHash().put("user:"+user.getId(),"id",user.getId().toString());
                    redisTemplate.opsForHash().put("user:"+user.getId(),"nickname",user.getNickname());
                    redisTemplate.expire("user:" + user.getId(), 2, TimeUnit.DAYS);
                    return new Result(true, StatusCode.OK, "自动登录成功",map1);
                }
            }
        }

         return new Result(false, StatusCode.ERROR,"自动登录失败！");
    }

    /**
     * 当token过期了刷新token
     * @param userId
     * @param nickname
     * @return
     */
    @GetMapping("/reflashToken")
    public Result reflashToken(@RequestParam("userId")String userId,@RequestParam("nickname") String nickname){
        String token = JWTUtils.createJWT(userId, nickname, null, "user");
        return new Result(true, StatusCode.OK, "刷新token成功", token);
    }


}
