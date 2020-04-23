package com.zls.controller;

import com.zls.pojo.Article;
import com.zls.pojo.User;
import com.zls.service.UserService;
import com.zls.vo.PageHome;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * 根据用户ID查询用户信息
     * @param id  userID
     * @return
     */
    @GetMapping("/find/{id}")
    public Result findUserById(@PathVariable("id") Integer id){
        User user = userService.findById(id);
        if (user!=null){
            return new Result(true, StatusCode.OK, "查询成功！", user);
        }
        return new Result(false, StatusCode.ERROR, "查询失败！");
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

    /**
     * 修改用户信息
     * @param user 用户实体
     * @return
     */
    @PutMapping("/update")
    public Result updateUser(@RequestBody User user) {
        boolean b = userService.updateUser(user);
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "修改失败！");
    }


    /**
     * 修改用户头像
     * @param user 用户实体
     * @param
     * @return
     */
    @PutMapping("/update/avatar")
    public Result updataUserAvatar(@RequestBody User user){
        boolean b = userService.updateUserAvatar(user.getId(),user.getAvatar());
        if (b) {
            return new Result();
        }
        return new Result(false, StatusCode.ERROR, "修改头像失败！");
    }

    /**
     * 查询用户主页中需要显示的信息
     * @param userId 用户ID
     */
    @GetMapping("/pageHome")
    public Result findUserPageHome(@RequestParam("userId") Integer userId){
        PageHome userAndOtherById = userService.findUserAndOtherById(userId);
        if (userAndOtherById != null) {
            return new Result(true, StatusCode.OK, "查询成功！", userAndOtherById);
        }
        return new Result(false, StatusCode.ERROR, "查询失败！");
    }

    /**
     * 根据用户ID查询收藏的文章
     * @param userId
     * @return
     */
    @GetMapping("/collection")
    public Result findCollectionArticleByUserId(@RequestParam("userId") Integer userId){
        List<Article> collectionArticleByUserId = userService.findCollectionArticleByUserId(userId);
        return new Result(true, StatusCode.OK, "执行成功！", collectionArticleByUserId);
    }

    /**
     * 根据用户ID查询关注列表
     * @param userId
     * @return
     */
    @GetMapping("/follow/list")
    public Result findFollowListById(@RequestParam("userId") Integer userId){
        List<User> followList = userService.findFollowListById(userId);
        return new Result(true, StatusCode.OK, "执行成功！", followList);
    }

    /**
     * 根据用户ID查询粉丝列表
     * @param userId
     * @return
     */
    @GetMapping("/fans/list")
    public Result findFansList(@RequestParam("userId") Integer userId){
        List<User> fansList = userService.findFansListById(userId);
        return new Result(true, StatusCode.OK, "执行成功！", fansList);
    }

    /**
     * 查询粉丝数排名前十的用户
     */
    @GetMapping("/find/hot")
    public Result findHotUser(){
        List<User> hotUser = userService.findHotUser();
        return new Result(true, StatusCode.OK, "查询成功", hotUser);
    }
}
