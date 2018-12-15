package com.seu.architecture.job;

import com.seu.architecture.service.LocalService;

import com.seu.architecture.service.impl.LocalServiceImpl;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by 17858 on 2017-11-20.
 */
public class ScanLocalImageJob extends QuartzJobBean {

    private LocalService localService;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        localService = new LocalServiceImpl();
        localService.scanImagePath();
    }
}
