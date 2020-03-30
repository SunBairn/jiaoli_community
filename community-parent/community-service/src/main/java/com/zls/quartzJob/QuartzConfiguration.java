package com.zls.quartzJob;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 任务配置类
 */
@Configuration
public class QuartzConfiguration {


    // 使用JobDetail包装Job
    @Bean
    public JobDetail myCronJobDetail(){
        return JobBuilder.newJob(MyJobViewCount.class).withIdentity("myCronJob").storeDurably().build();
    }

    // 将JobDetial注册到Cron表达式的Trigger上去
    @Bean
    public Trigger myTrigger(){
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("* * 3 * * ?");
        return TriggerBuilder.newTrigger().forJob(myCronJobDetail()).withIdentity("myTrigger").withSchedule(cronScheduleBuilder).build();
    }
}
