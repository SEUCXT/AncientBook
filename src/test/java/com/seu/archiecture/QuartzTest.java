package com.seu.archiecture;

import com.seu.architecture.ArchitectureApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Created by 17858 on 2017-09-25.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchitectureApplication.class)
public class QuartzTest {

    @Test
    public void addJob() throws Exception {
//QuartzUtils.addCronJob("SampleJob", "jobGroup1", "0 45 16 ? * * 2017");
        // 通过SchedulerFactory获取一个调度器实例
        String jobClassName = "SampleJob";
        String jobGroupName = "group1";
        String cronExpression = "0 47 10 ? * * 2017";
        SchedulerFactory sf = new StdSchedulerFactory();

        Scheduler sched = sf.getScheduler();

        // 启动调度器
        sched.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                .withSchedule(scheduleBuilder).build();

        try {
            sched.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            System.out.println("创建定时任务失败" + e);
            throw new Exception("创建定时任务失败");
        }
    }

    private static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job) class1.newInstance();
    }
}
