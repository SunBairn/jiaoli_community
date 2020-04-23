package com.zls.service.impl;

import com.zls.mapper.UserMapper;
import com.zls.pojo.Article;
import com.zls.pojo.User;
import com.zls.service.UserService;
import com.zls.vo.PageHome;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.MD5Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Value("${user.avatar}")
    String avatar;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    UserMapper userMapper;

    /**
     * 发送短信验证码
     * @param mobile
     */
    @Override
    public void sendSms(String mobile) {
        //1.生成6位短信验证码
        Random random = new Random();
        int max = 999999;
        int min = 100000;
        int code = random.nextInt(max);// 随机生成
        if (code<min){
            code=code+min;
        }
        System.out.println("短信验证码为："+code);
        //2、将验证码放入Redis中
        stringRedisTemplate.opsForValue().set("smscode_"+mobile,code+"",5, TimeUnit.MINUTES);//设置过期时间为5分钟

        //3、将手机验证码发送到rabbitmq中
        Map<String,String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("code", code+"");
        rabbitTemplate.convertAndSend("sms",map);
    }


    /**
     * 校验用户是否已经被注册过了,如果返回为true，则该用户未注册过
     * @param mobile
     * @return boolean
     */
    @Override
    public boolean checkUser(String mobile){
        User user=userMapper.findByMobile(mobile);
        if (user != null) {
            return false;
        }
        return true;
    }

    /**
     * 注册用户
     * @param user
     * @param code
     */
    @Override
    public void addUser(User user, String code) {
        User user1 = userMapper.findByMobile(user.getMobile());
        if (user1==null){
            // 1、判断验证码是否正确
            String syscode = (String) redisTemplate.opsForValue().get("smscode_" + user.getMobile());
            if (syscode == null) {
                throw new RuntimeException("请点击获取验证码");
            }
            if (!syscode.equals(code)) {
                throw new RuntimeException("验证码不正确");
            }
            String password = MD5Utils.MD5Encode(user.getPassword());
            user.setPassword(password);
            user.setNickname("jiaoli_"+user.getMobile());
            user.setGmtRegdate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
            user.setAvatar(avatar);
            userMapper.addUser(user);
        }else{
            throw new RuntimeException("该号码已被注册");
        }

    }


    /**
     * 用户登录
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> findByMobileAndPassword(Map<String, String> map,String oldJiaoliToken) {
        String mobile = map.get("mobile");
        String password = map.get("password");
        String MD5password = MD5Utils.MD5Encode(password);
        User user = userMapper.findByMobileAndPassword(mobile,MD5password);
        if (user!=null){
            String jiaoliToken = UUID.randomUUID().toString();
            String s = null;
            // 存的自动登录所需要的token
            if (!StringUtils.isEmpty(oldJiaoliToken)){
                 s = stringRedisTemplate.opsForValue().get("jiaoliToken_" + oldJiaoliToken);
            }
            if (s!=null&&s.equals(oldJiaoliToken)){
                // 这里是防止用户重新登录后缓存中会多出很多没用jiaolitoken
                // 这里必须要将老的jiaolitoken设置过期
                // 这里是用户重新登录进来后得到了新的jiaolitoken,所以我们需要更新缓存中的token
                Boolean expire = stringRedisTemplate.expire("jiaoliToken_" + oldJiaoliToken, -1, TimeUnit.MINUTES.SECONDS);
                Boolean expire1 = stringRedisTemplate.expire(oldJiaoliToken, -1, TimeUnit.MINUTES.SECONDS);
                if (!expire || !expire1) {
                    throw new CustomizeException(CustomizeErrorCode.REDIS_EXPIRE_ERROR);
                }
            }

            // 存储的用于下次自动登录的jiaolitoken
            stringRedisTemplate.opsForValue().set("jiaoliToken_"+jiaoliToken,jiaoliToken,20,TimeUnit.DAYS);

            // 存的用户ID，用与直接可以通过缓存获取用户信息
            stringRedisTemplate.opsForValue().set(jiaoliToken,user.getId().toString(),20,TimeUnit.DAYS);
            // 登录成功后将用户的id和昵称存到redis中，便于生成jwttoken时使用(过期时间为2天)
            if (redisTemplate.opsForHash().hasKey("user:"+user.getId(),"id")==true){
                // 如果用户信息已经在缓存中，则直接刷新过期时间即可
                redisTemplate.expire("user:" + user.getId(), 2, TimeUnit.DAYS);
            }else{
                redisTemplate.opsForHash().put("user:"+user.getId(),"id",user.getId());
                redisTemplate.opsForHash().put("user:"+user.getId(),"nickname",user.getNickname());
                redisTemplate.expire("user:" + user.getId(), 2, TimeUnit.DAYS);
            }
            Map<String, Object> map2 = new HashMap<>();
            map2.put("user", user);
            map2.put("jiaoliToken", jiaoliToken);
            return map2;
        }
        return null;

    }

    /**
     * 用户自动登录,如果可以则直接根据用户的ID查询用户的信息
     * @param jiaoliToken
     * @return
     */
    public Map<String, Object> autoLogin(String jiaoliToken){
            // 将传过来的token缓存中比对
            String s = stringRedisTemplate.opsForValue().get("jiaoliToken_" + jiaoliToken);
            if (s!=null){
                 String userId = stringRedisTemplate.opsForValue().get(jiaoliToken);
                 if (userId!=null){
                     // 根据从缓存中取出的用户ID去mysql数据库中查询用户信息
                     User user = userMapper.findById(Integer.valueOf(userId));
                     if (user!=null){
                         // 如果用户不为空，则存入Redis中
                         // 生成一个新的jiaoliToken
                         String jiaoliToken1 = UUID.randomUUID().toString();
                         // 存的自动登录所需要的token
                         stringRedisTemplate.opsForValue().set("jiaoliToken_"+jiaoliToken1,jiaoliToken1,20,TimeUnit.DAYS);
                         // 将原先redis中的token设置过期
                         Boolean expire = stringRedisTemplate.expire("jiaoliToken_" + jiaoliToken, -1, TimeUnit.SECONDS);

                         if (!expire){
                             throw new CustomizeException(CustomizeErrorCode.REDIS_EXPIRE_ERROR);
                         }
                         // 存的用户ID，用与直接可以通过缓存获取用户信息
                         stringRedisTemplate.opsForValue().set(jiaoliToken1,user.getId().toString(),20,TimeUnit.DAYS);
                         // 将原先的用户ID设置过期
                         Boolean expire1 = stringRedisTemplate.expire(jiaoliToken, -1, TimeUnit.SECONDS);
                         if (!expire1) {
                             throw new CustomizeException(CustomizeErrorCode.REDIS_EXPIRE_ERROR);
                         }
                         // 存的用户信息
                         // 登录成功后将用户的id和昵称存到redis中，便于生成jwttoken时使用(过期时间为2天)
                         if (redisTemplate.opsForHash().hasKey("user:"+user.getId(),"id")==true){
                             // 如果用户信息已经在缓存中，则直接刷新过期时间即可
                             redisTemplate.expire("user:" + user.getId(), 2, TimeUnit.DAYS);
                         }else {
                             redisTemplate.opsForHash().put("user:"+user.getId(),"id",user.getId());
                             redisTemplate.opsForHash().put("user:"+user.getId(),"nickname",user.getNickname());
                             redisTemplate.expire("user:" + user.getId(), 2, TimeUnit.DAYS);
                         }
                         Map<String, Object> map2 = new HashMap<>();
                         map2.put("user", user);
                         map2.put("jiaoliToken", jiaoliToken1);
                         return map2;
                     }
                 }
            }

        return null;
    }

    /**
     * 退出登录
     * @param userId  用户ID
     * @param jiaoliToken 自动登录token
     * @return
     */
    @Override
    public boolean loginOut(Integer userId,String jiaoliToken) {
        // 1、清除缓存中的用户信息
        stringRedisTemplate.expire("jiaoliToken_" + jiaoliToken, -1, TimeUnit.SECONDS);
        stringRedisTemplate.expire(jiaoliToken, -1, TimeUnit.SECONDS);
        redisTemplate.expire("user_" + userId,-1,TimeUnit.SECONDS);
        return false;
    }


    /**
     * 根据用户ID去查询某个用户的信息
     * @param id  userId
     * @return
     */
    public User findById(Integer id){
        User user = userMapper.findById(id);
        return user;
    }

    /**
     * 根据用户的ID去删除某个用户
     * @param id
     */
    public void deleteUser(Integer id){
        userMapper.deleteUser(id);
    }

    /**
     * 修改用户信息
     * @param user 用户实体
     * @return
     */
    @Override
    public boolean updateUser(User user) {
        Date birthday = user.getBirthday();
        boolean b = userMapper.updateUser(user);
        return b;
    }


    /**
     * 修改用户头像
     * @param id userId
     * @param avatar 用户头像
     * @return
     */
    @Override
    public boolean updateUserAvatar(Integer id, String avatar) {
        boolean b = userMapper.updateUserAvatar(id, avatar);
        return b;
    }

    /**
     * 根据用户ID获取用户的信息和用户主页需要显示的信息
     * @param id userId
     */
    @Override
    public PageHome findUserAndOtherById(Integer id) {
        PageHome pageHome = userMapper.findUserAndOtherById(id);
        return pageHome;
    }

    /**
     * 根据用户ID查询收藏的文章
     * @param userId
     * @return
     */
    @Override
    public List<Article> findCollectionArticleByUserId(Integer userId) {
        List<Article> collectionArticleByUserId = userMapper.findCollectionArticleByUserId(userId);
        return collectionArticleByUserId;
    }

    /**
     * 根据ID查询用户关注列表
     * @param id 用户ID
     * @return
     */
    @Override
    public List<User> findFollowListById(Integer id) {
        List<User> followList = userMapper.findFollowListById(id);
        return followList;
    }

    /**
     * 根据ID查询用户粉丝列表
     * @param id 用户ID
     * @return
     */
    @Override
    public List<User> findFansListById(Integer id) {
        List<User> fansList = userMapper.findFansListById(id);
        return fansList;
    }

    /**
     * 查询粉丝数排名前十的用户信息
     * @return
     */
    @Override
    public List<User> findHotUser() {
        // 先从缓存中查，看缓存中有没有
        Set<String> keys = stringRedisTemplate.keys("hot:user:*");
        if (keys.size()!=0&&keys.size()==16){
            List<User> users = new ArrayList<>();
            for (String key : keys) {
                String id = (String) stringRedisTemplate.opsForHash().get(key, "id");
                String nickname = (String) stringRedisTemplate.opsForHash().get(key, "nickname");
                String avatar = (String) stringRedisTemplate.opsForHash().get(key, "avatar");
                User user=new User();
                user.setId(Integer.valueOf(id));
                user.setNickname(nickname);
                user.setAvatar(avatar);
                users.add(user);
            }
            return users;
        }
        // 否者从数据库中查询，在存入缓存中，过期时间为1天
        List<User> hotUser = userMapper.findHotUser();
        Map<String, String> map = new HashMap<>();
        for (User user : hotUser) {
            map.put("id", user.getId().toString());
            map.put("nickname", user.getNickname());
            map.put("avatar", user.getAvatar());
            stringRedisTemplate.opsForHash().putAll("hot:user:"+user.getId(),map);
            stringRedisTemplate.expire("hot:user", 1, TimeUnit.DAYS);
        }
        return hotUser;
    }


}
