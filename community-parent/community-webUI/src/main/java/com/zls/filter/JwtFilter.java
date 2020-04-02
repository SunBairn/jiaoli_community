package com.zls.filter;

import enums.CustomizeErrorCode;
import enums.CustomizeException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import utils.JWTUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 权限验证拦截器拦截器
 */
@Component
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
public class JwtFilter extends HandlerInterceptorAdapter {
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("经过了拦截器");
        final String authorzation = request.getHeader("Authorzation");
        if (authorzation != null && authorzation.startsWith("Bearer ")) {
            final String token = authorzation.substring(7);
            Claims claims = null;
            try {
                claims = JWTUtils.parseJWT(token);
            } catch (Exception e) {
                // 判断是否过期且需不需要刷新token
                // 1、从Redis中取出用户数据，如果为空，说明用户没有登录，不生成token
                String id = request.getHeader("id");
                Map entries = redisTemplate.opsForHash().entries("user:" + id);
                if (!entries.isEmpty()){
                    // 2、如果不为空，则刷新token
                    String jwttoken = JWTUtils.createJWT( entries.get("id")+"", entries.get("nickname")+"", null, "user");
                    // 3、重新生成jwttoken后重置redis中的用户信息
                    redisTemplate.opsForHash().put("user:"+entries.get("id"),"id",entries.get("id"));
                    redisTemplate.opsForHash().put("user:"+entries.get("id"),"nickname",entries.get("nickname"));
                    redisTemplate.expire("user:" + entries.get("id"), 2, TimeUnit.DAYS);
                    // 4、将jwt存到cookie中
                    Cookie cookie=new Cookie("jwttoken",token);
                    cookie.setMaxAge(1000*60*60*2);
                    response.addCookie(cookie);
                    try {
                        // 重新解析给用户权限
                        claims = JWTUtils.parseJWT(jwttoken);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                // 这个抛出的异常是第一次进来jwttoken解析的异常
               // e.printStackTrace();
            }
            if (claims != null) {
                if ("admin".equals(claims.get("roles"))) {
                    request.setAttribute("admin_roles", claims);
                }
                if ("user".equals(claims.get("roles"))) {
                    request.setAttribute("user_roles", claims);
                }
            }
        }
        return true;
    }



}
