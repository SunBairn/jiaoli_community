import com.zls.MapperApplication;
import com.zls.mapper.*;
import com.zls.pojo.*;
import entity.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MapperApplication.class)
public class MapperApplicationTest {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    GatheringMapper gatheringMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionCommentMapper questionCommentMapper;

    @Autowired
    ArticleMapper articleMapper;
//    @Test
//    public void insertQuestion(){
//        Question question = new Question();
//        question.setTitle("问题1");
//        question.setContent("这套Java视频教程怎么样？");
//        question.setCreator(1);
//        question.setGmtCreate(System.currentTimeMillis());
//        question.setGmtModified(System.currentTimeMillis());
//        question.setType(2);
//        questionMapper.insertQuestion(question);
//    }
//
//    @Test
//    public void findAllQuestion(){
//        Page<Question> page = new Page();
//        page.setStart(2);
//        page.setSize(5);
//        List<Question> questions = questionMapper.findAllQuestion(2, (long) 2,5);
//
//        System.out.println(questions);
//    }

    @Test
    public void findAllGathering(){
        Page<Question> page = new Page();
        page.setStart(2);
        page.setSize(5);
        List<Article> questions = articleMapper.findAllArticle((long) 0, 5);

        System.out.println(questions);
    }


    @Test
    public void aaa(){
        Map<Integer, Integer> map = new HashMap<>();
        map.put(29, 30);
        map.put(30, 40);
        questionMapper.syncViewCountToDatabase(map);
        System.out.println("hahahah");

    }
}
