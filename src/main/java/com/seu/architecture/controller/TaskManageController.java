package com.seu.architecture.controller;

import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 17858 on 2017-09-22.
 */
@Controller
@RequestMapping("/TaskManagement")
public class TaskManageController {

    @Autowired
    TaskService taskService;

    /**
     * 添加任务
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @return
     * @throws Exception
     */
    @RequestMapping("/addTask")
    @ResponseBody
    public ResponseEntity<ViewObject> addTask(@RequestParam("jobClassName") String jobClassName,
                                              @RequestParam("jobGroupName") String jobGroupName,
                                              @RequestParam("cronExpression") String cronExpression,
                                              @RequestParam("description") String description) throws Exception {

//        String jobClassName = "com.seu.architecture.job.SampleJob3";
//        String jobGroupName = "group1";
//        String cronExpression = "0 12 14 ? * * 2017";
        String className = "com.seu.architecture.job." + jobClassName;
        ViewObject vo = taskService.addTask(className, jobGroupName, cronExpression,description);
        return ResponseEntity.ok(vo);
    }

    /**
     * 暂停任务
     * @param jobClassName
     * @param jobGroupName
     * @return
     */
    @RequestMapping("/pauseTask")
    @ResponseBody
    public ResponseEntity<ViewObject> pauseTask(@RequestParam("jobClassName") String jobClassName,
                                                @RequestParam("jobGroupName") String jobGroupName) {
        String className = "com.seu.architecture.job." + jobClassName;
        ViewObject vo = taskService.pauseTask(className, jobGroupName);
        return ResponseEntity.ok(vo);
    }

    /**
     * 重新安排任务
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @return
     */
    @RequestMapping("/rescheduleTask")
    @ResponseBody
    public ResponseEntity<ViewObject> rescheduleTask(@RequestParam("jobClassName") String jobClassName,
                                                     @RequestParam("jobGroupName") String jobGroupName,
                                                     @RequestParam("cronExpression") String cronExpression) {
        String className = "com.seu.architecture.job." + jobClassName;
        ViewObject vo = taskService.rescheduleTask(className, jobGroupName, cronExpression);
        return ResponseEntity.ok(vo);
    }

    /**
     * 删除任务
     * @param jobClassName
     * @param jobGroupName
     * @return
     */
    @RequestMapping("/deleteTask")
    @ResponseBody
    public ResponseEntity<ViewObject> deleteTask(@RequestParam("jobClassName") String jobClassName,
                                                 @RequestParam("jobGroupName") String jobGroupName) {
        String className = "com.seu.architecture.job." + jobClassName;
        ViewObject vo = taskService.deleteTask(className, jobGroupName);
        return ResponseEntity.ok(vo);
    }

    /**
     * 重新调度任务
     * @param jobClassName
     * @param jobGroupName
     * @return
     */
    @RequestMapping("/resumeTask")
    @ResponseBody
    public ResponseEntity<ViewObject> resumeTask(@RequestParam("jobClassName") String jobClassName,
                                                 @RequestParam("jobGroupName") String jobGroupName) {
        String className = "com.seu.architecture.job." + jobClassName;
        ViewObject vo = taskService.resumeTask(className, jobGroupName);
        return ResponseEntity.ok(vo);
    }

}
