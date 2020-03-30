package com.zls.quartzJob;

import com.zls.mapper.QuestionMapper;
import enums.CustomizeErrorCode;
import enums.CustomizeException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.*;


/**
 * 任务类，将阅读数同步到数据库中
 */
public class MyJobLikeCount extends QuartzJobBean {


    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    QuestionMapper questionMapper;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Map<Integer, Integer> map = new HashMap<>();
        Set<String> keys = stringRedisTemplate.keys("question:view_count:*");
        System.out.println(keys.toString());
        if (keys != null) {
            Iterator<String> iterator = keys.iterator(); // 遍历所有的key
            while (iterator.hasNext()) {
                String next = iterator.next();   // 获取具体的每一个key
                String s = stringRedisTemplate.opsForValue().get(next);  // 获取redis中key 所对应的的值
                Integer likeCount = Integer.valueOf(s);
                String substring = next.substring(20);
                Integer questionId = Integer.valueOf(substring);
                map.put(questionId, likeCount);
            }
            Boolean aBoolean = questionMapper.syncViewCountToDatabase(map);
            if (!aBoolean) {
                throw new CustomizeException(CustomizeErrorCode.REDIS_SYNC_ERROR);
            }
        }


    }
}
