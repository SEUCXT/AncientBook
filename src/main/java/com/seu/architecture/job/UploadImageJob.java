package com.seu.architecture.job;

import com.seu.architecture.config.Constants;
import com.seu.architecture.service.PdfService;
import com.seu.architecture.service.impl.PdfServiceImpl;
import com.sun.tools.internal.jxc.ap.Const;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 17858 on 2017-11-16.
 */
public class UploadImageJob extends QuartzJobBean {

    private PdfService pdfService;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        File originPath = new File(Constants.QUARTZ_PATH,"pdf");
        File imageList[] = originPath.listFiles();
        for (int i = 0; i < imageList.length; i++) {
            //获取每本书文件夹，文件夹的名称是书名
                String bookId = imageList[i].getName();
                String bookPdfPath = imageList[i].getAbsolutePath(); //每本书文件夹的绝对路径
                pdfToImageJob(bookId,bookPdfPath);
        }

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Exec Time Is : " + sf.format(date));

        System.out.println("Hello Job!");
    }

    private List<File> getAllPDFpath(String rootPath) {
        File originPath = new File(rootPath);
        File imageList[] = originPath.listFiles();
        List<File> list = new ArrayList<>();
        for (int i = 0; i < imageList.length; i++) {
            list.add(imageList[i]);
        }
        return list;
    }

    private void pdfToImageJob(String bookid, String rootPath) {
        List<File> pdfList = getAllPDFpath(rootPath);
        pdfService = new PdfServiceImpl();
        File targetFile = new File(Constants.QUARTZ_PATH+"\\img", bookid);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            for (int i = 0; i < pdfList.size(); i++) {
                pdfService.pdfToImage(pdfList.get(i).getAbsolutePath(), targetFile.getAbsolutePath(), i + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
