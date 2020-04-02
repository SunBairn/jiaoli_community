package com.zls.confige;

import com.zls.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册拦截器
 */
@Configuration
public class ApplicationConfig extends WebMvcConfigurationSupport {

    @Autowired
    JwtFilter jwtFilter;

    public void addInterceptors(InterceptorRegistry registry) {
        List<String> list = new ArrayList<>();
        list.add("/**/login");
        list.add("/**/autoLogin");
        list.add("/**/find/**");
        list.add("/**/user/check");
        registry.addInterceptor(jwtFilter)
                .addPathPatterns("/**")  //需要拦截的路径
                .excludePathPatterns(list);  // 不需要拦截的路径

    }

}
