package com.seu.architecture.service;

import com.seu.architecture.model.ViewObject;

/**
 * Created by 17858 on 2017-09-22.
 */
public interface TaskService {


    ViewObject addTask(String jobClassName, String jobGroupName, String cronExpression, String description) throws Exception;

    ViewObject pauseTask(String jobClassName, String jobGroupName);

    ViewObject deleteTask(String jobClassName, String jobGroupName);

    ViewObject resumeTask(String jobClassName, String jobGroupName);

    ViewObject rescheduleTask(String jobClassName, String jobGroupName, String cronExpression);
}
