package com.zls.filter;

import io.jsonwebtoken.Claims;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import utils.JWTUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断是否要刷新jwttoken的拦截器，拦截任何请求
 */
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@Component
public class ReflashJwtTokenFilter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("经过了是否要刷新jwttoken的拦截器");
        final String authorzation = request.getHeader("Authorzation");
        if (authorzation != null && authorzation.startsWith("Bearer ")) {
            final String token = authorzation.substring(7);
           if(!"null".equals(token)) {
               Claims claims = null;
               try {
                   claims = JWTUtils.parseJWT(token);
               } catch (Exception e) {
                   // 这里说名token过期了或者错误,设置一个标志，写入到cookie中，当reflash_token为true时表示token无效了，需要刷新jwttoken了
                   Cookie cookie = new Cookie("reflash_token", "true");
                   cookie.setDomain("localhost");
                   cookie.setPath("/");
                   cookie.setHttpOnly(false);
                   cookie.setMaxAge(60 * 60 * 24 * 20);
                   response.addCookie(cookie);
                   e.printStackTrace();
               }
           }
        }
        return true;
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response,
                                               Object handler) throws Exception {
    }

}
