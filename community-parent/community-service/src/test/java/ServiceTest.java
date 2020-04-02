import com.zls.ServiceApplication;
import com.zls.pojo.Question;
import com.zls.pojo.QuestionComment;
import com.zls.service.QuestionCommentService;
import com.zls.service.QuestionService;
import com.zls.utils.RedisServiceExtend;
import entity.Page;
import org.apache.ibatis.annotations.Arg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class ServiceTest {
    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionCommentService questionCommentService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisTemplate<String,String> redisTemplate1;
    @Autowired
    RedisServiceExtend redisServiceExtend;

    @Test
    public void test01(){
        Page<Question> page=questionService.findAllQuestion(2,2,"hot");
        System.out.println(page.getTotal());
    }


    @Test
    public void test02(){
//        stringRedisTemplate.opsForValue().set("aa", String.valueOf(2));
//        stringRedisTemplate.opsForValue().increment("aa");
//        String aa = stringRedisTemplate.opsForValue().get("aa");
//        stringRedisTemplate.expire("aa", -1, TimeUnit.SECONDS);
//        System.out.println(aa);

        // 获取所有的key,根据匹配规则
//        Set<String> s = stringRedisTemplate.keys("question:view_count:*");
//
//        Iterator<String> iterator = s.iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next().substring(20));
//        }

//        stringRedisTemplate.opsForValue().setBit("key", 1, true);
//        stringRedisTemplate.opsForValue().setBit("key", 0, true);
//        stringRedisTemplate.opsForValue().setBit("key", 2, false);
//        stringRedisTemplate.opsForValue().setBit("key", 3, true);
//        stringRedisTemplate.opsForValue().setBit("key", 4, false);
//        stringRedisTemplate.opsForValue().setBit("key", 5, true);
//        stringRedisTemplate.opsForValue().setBit("key", 6, false);
//        stringRedisTemplate.opsForValue().setBit("key", 7, true);
//        stringRedisTemplate.opsForValue().setBit("key", 8, false);
//        System.out.println(stringRedisTemplate.opsForValue().getBit("key",2));
//
//        System.out.println(redisServiceExtend.bitCount("key"));

        QuestionComment questionComment = new QuestionComment();
        questionComment.setCommentator(5);
        questionComment.setContent("很棒！");
        questionComment.setGmtCreate(System.currentTimeMillis());
        questionComment.setParentId(29);
        questionComment.setType(1);
        questionCommentService.addComment(questionComment);
    }


}
