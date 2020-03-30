import com.zls.ServiceApplication;
import com.zls.pojo.Question;
import com.zls.service.QuestionService;
import entity.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class ServiceTest {
    @Autowired
    QuestionService questionService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

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
        Set<String> s = stringRedisTemplate.keys("question:view_count:*");

        Iterator<String> iterator = s.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().substring(20));
        }
    }


}
