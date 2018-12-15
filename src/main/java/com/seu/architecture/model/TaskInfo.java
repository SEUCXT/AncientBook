package com.seu.architecture.model;

import javax.persistence.*;

/**
 * Created by 17858 on 2017-09-22.
 */
@Entity
@Table(name = "taskinfo")
public class TaskInfo{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /**任务名称*/
    @Column(name = "jobName", unique = true, nullable = false)
    private String jobName;

    /**任务分组*/
    @Column(name = "jobGroup", nullable = false)
    private String jobGroup;

    /**任务描述*/
    @Column(name = "jobDescription", nullable = false)
    private String jobDescription;

    /**任务状态*/
    @Column(name = "jobStatus", nullable = false)
    private String jobStatus;

    /**任务表达式*/
    @Column(name = "cronExpression", nullable = false)
    private String cronExpression;

    @Column(name = "createTime", nullable = false)
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
