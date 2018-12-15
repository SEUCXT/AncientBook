package com.seu.architecture.service.impl;

import com.seu.architecture.model.TaskInfo;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.repository.TaskRepository;
import com.seu.architecture.service.TaskService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by 17858 on 2017-09-22.
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static Scheduler scheduler;

    /**
     * 外部设置该工具类使用的 Scheduler 实例
     *
     * @param scheduler
     */
    public static synchronized void setScheduler(Scheduler scheduler) {
        TaskServiceImpl.scheduler = scheduler;
    }

    /**
     * 获取该工具类使用的 Scheduler 实例，如果外部没有设置该工具的此属性，该工具类内部先从 StdSchedulerFactory 创建一个 Scheduler 到此属性字段
     *
     * @return
     */
    public static Scheduler getScheduler() {
        if (scheduler == null) {
            synchronized (TaskServiceImpl.class) {// 如果 scheduler 为空尝试锁住当前类然后初始化其 scheduler 属性
                if (scheduler == null) { // 如果锁定当前类之后 scheduler 仍然为空，说明已经安全排斥了其他其他同步方法同时设置该属性的可能性，下面对该属性 scheduler 进行设置
                    scheduler = createStdScheduler();
                }
            }
        }
        return scheduler;
    }

    private static Scheduler createStdScheduler() {
        try {
            // 配置文件的加载规则 :
            // 1. 尝试 system.environment 中配置属性 org.quartz.properties 作为配置文件
            // 2. 尝试读取当前目录的文件 quartz.properties 作为配置文件
            // 3. 尝试使用当前 classLoader.getResourceAsStream("quartz.properties") 作为配置文件
            // 4. 尝试使用当前 classLoader.getResourceAsStream("/quartz.properties") 作为配置文件
            // 5. 如果以上尝试均失败，使用 quartz jar 包中自己提供的缺省配置 org/quartz/quartz.properties, 这个缺省配置会使用 RAMStore 保存信息
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            return scheduler;
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加定时任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     */
    @Override
    public ViewObject addTask(String jobClassName, String jobGroupName, String cronExpression, String description) throws Exception {
        ViewObject vo = new ViewObject();

        Scheduler scheduler = getScheduler();
        // 启动调度器
        scheduler.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName)
                .usingJobData("job_add_thread", Thread.currentThread().getName()) // 缺省参数 1
                .usingJobData("job_create_time", LocalDateTime.now().toString()) // 缺省参数 2
                .build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withSchedule(scheduleBuilder)
                .usingJobData("trigger_add_thread", Thread.currentThread().getName()) // 缺省参数 1
                .usingJobData("trigger_create_time", LocalDateTime.now().toString()) // 缺省参数 2
                .build();

        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setJobName(jobClassName);
        taskInfo.setJobGroup(jobGroupName);
        taskInfo.setJobDescription(description);
        taskInfo.setCronExpression(cronExpression);
        taskInfo.setJobStatus("running");
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        taskInfo.setCreateTime(sf.format(date));
        taskRepository.save(taskInfo);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            vo.set(ViewObject.ERROR, 0);
        } catch (SchedulerException e) {
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
            throw new Exception("创建定时任务失败");
        }
        return vo;
    }

    /**
     * 重新调度一个 cron job
     *
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    @Override
    public ViewObject rescheduleTask(String jobClassName, String jobGroupName, String cronExpression) {
        ViewObject vo = new ViewObject();
        try {
            Scheduler scheduler = getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
                    .usingJobData("trigger_add_thread", Thread.currentThread().getName()) // 缺省参数 1
                    .usingJobData("trigger_create_time", LocalDateTime.now().toString()) // 缺省参数 2
                    .build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);

            TaskInfo taskInfo = taskRepository.findByJobName(jobClassName);
            taskInfo.setCronExpression(cronExpression);
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            taskInfo.setCreateTime(sf.format(date));
            taskRepository.save(taskInfo);

            vo.set(ViewObject.ERROR, 0);
        } catch (SchedulerException e) {
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
            throw new RuntimeException("更新定时任务失败");
        }
        return vo;
    }

    /**
     * 从调度器中删除一个Job
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    @Override
    public ViewObject deleteTask(String jobClassName, String jobGroupName) {
        ViewObject vo = new ViewObject();
        Scheduler scheduler = getScheduler();
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
            vo.set(ViewObject.ERROR, 0);
        } catch (Exception e) {
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
            throw new RuntimeException(e);

        }
        TaskInfo taskInfo = taskRepository.findByJobName(jobClassName);
        taskRepository.delete(taskInfo);

        return vo;
    }

    /**
     * 暂停定时任务
     *
     * @param jobClassName
     * @param jobGroupName
     */
    @Override
    public ViewObject pauseTask(String jobClassName, String jobGroupName) {
        ViewObject vo = new ViewObject();
        try {
            Scheduler scheduler = getScheduler();
            scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));

            TaskInfo taskInfo = taskRepository.findByJobName(jobClassName);
            taskInfo.setJobStatus("pause");
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            taskInfo.setCreateTime(sf.format(date));
            taskRepository.save(taskInfo);

            vo.set(ViewObject.ERROR, 0);
        } catch (Exception e) {
            e.printStackTrace();
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return vo;
    }

    /**
     * 继续调度一个Job
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    @Override
    public ViewObject resumeTask(String jobClassName, String jobGroupName) {
        ViewObject vo = new ViewObject();
        try {
            Scheduler scheduler = getScheduler();
            scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
            TaskInfo taskInfo = taskRepository.findByJobName(jobClassName);
            taskInfo.setJobStatus("running");
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            taskInfo.setCreateTime(sf.format(date));
            taskRepository.save(taskInfo);

            vo.set(ViewObject.ERROR, 0);
        } catch (Exception e) {
            e.printStackTrace();
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return vo;
    }

    private static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job) class1.newInstance();
    }

}
