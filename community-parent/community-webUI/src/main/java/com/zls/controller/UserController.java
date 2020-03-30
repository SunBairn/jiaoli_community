package com.zls.controller;

import com.zls.pojo.User;
import com.zls.service.UserService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/sendsms/{mobile}",method= RequestMethod.GET)
    public Result sendsms(@PathVariable("mobile") String mobile){
        userService.sendSms(mobile);
        return new Result(true, StatusCode.OK, "发送验证码成功！");
    }


    /**
     *  用户注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("/add")
    public Result addUser(@RequestBody User user,@RequestParam("code") String code,
                          @RequestParam("confirmPassword") String confirmPassword){
        // 校验两次密码是否一致
        if(!confirmPassword.equals(user.getPassword())){
            return new Result((false), StatusCode.ERROR, "密码与确认密码不一致！");
        }
        userService.addUser(user,code);
        return new Result(true, StatusCode.OK, "注册用户成功！");
    }


    /**
     * 检查某个号码是否已经注册过用户了
     * @param mobile
     * @return
     */
    @GetMapping("/check")
    public Result checkUser(@RequestParam("mobile") String mobile){
        boolean flag = userService.checkUser(mobile);
        if (flag==true){
            return new Result(true, StatusCode.OK, "该号码未被注册，可以注册");
        }
        return new Result(false, StatusCode.ERROR, "该号码已被注册");
    }


    /**
     * 根据ID删除用户
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delectUser(@PathVariable Integer id, HttpServletRequest request){
        // 检查用户是否为管理员，否者不能操作
        Claims claims = (Claims) request.getAttribute("admin_roles");
        if (claims==null){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        userService.deleteUser(id);
        return new Result(true, StatusCode.OK, "删除用户成功！");
    }



}
