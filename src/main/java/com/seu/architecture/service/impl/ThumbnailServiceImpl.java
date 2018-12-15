package com.seu.architecture.service.impl;

import com.seu.architecture.config.Constants;
import com.seu.architecture.service.ThumbnailService;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;


import static com.seu.architecture.config.Constants.THUMBNAIL_WIDTH;

@Service
public class ThumbnailServiceImpl implements ThumbnailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThumbnailServiceImpl.class);

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Override
    public String thumbnailName(String fileName, int width, int height) {
        int dotPos = fileName.lastIndexOf(".");
        String ext = "jpg";
        if (dotPos >= 0) {
            ext = fileName.substring(dotPos + 1);
            fileName = fileName.substring(0, dotPos);
        }
        return String.format("%s_%d_%d.%s", fileName, width, height, ext);
    }

    @Override
    public void submitThumbnailJobs(List<String> fileNames) {
        for (String fileName : fileNames) {
            submitThumbnailJob(fileName);
        }
    }

    @Override
    public Future<?> submitThumbnailJob(String imageName) {
        return taskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                thumbnail(imageName, THUMBNAIL_WIDTH, Constants.THUMBNAIL_HEIGHT);
            }
        });
    }

    @Override
    public void thumbnail(String imageName, int width, int height) {
        LOGGER.info(String.format("Thumbnailing image %s", imageName));
        try {
            Thumbnails.of(Constants.UPLOAD_DIR + imageName)
                    .size(width, height)
                    .outputFormat("jpg")
                    .toFile(Constants.UPLOAD_DIR + thumbnailName(imageName, width, height));
        } catch (IOException e) {
            LOGGER.info(String.format("Thumbnailing image %s failed %s", imageName, e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public void thumbnailSave(String thumbnailsPath, String originImagePath, String imageName, int width, int height) {
        LOGGER.info(String.format("Thumbnailing image %s", imageName));
        try {
            Thumbnails.of(originImagePath + imageName)
                    .size(width, height)
                    .outputFormat("jpg")
                    .toFile(thumbnailsPath + thumbnailName(imageName, width, height));
        } catch (IOException e) {
            LOGGER.info(String.format("Thumbnailing image %s failed %s", imageName, e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public String getThumbnailPath(String thumbnailsPath, String imagePath, String imageName, int width, int height) {
        LOGGER.info(String.format("Thumbnailing image %s", imagePath));
        String res = thumbnailsPath + '\\' + thumbnailName(imageName, width, height);
        try {
            Thumbnails.of(imagePath)
                    .size(width, height)
                    .outputFormat("jpg")
                    .toFile(res);
        } catch (IOException e) {
            LOGGER.info(String.format("Thumbnailing image %s failed %s", imageName, e.getMessage()));
            e.printStackTrace();
        }
        File file = new File(res);
        if(!file.exists()){
            return "";
        }
        return res;
    }

    @Override
    public List<String> image2Thumbnails(String thumbnailsPath, String bookName, List<String> imageList) {
        int size = imageList.size();
        List<String> thumbnailList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            thumbnailList.add(getThumbnailPath(thumbnailsPath, imageList.get(i), String.format("%s_%d", bookName, i), Constants.THUMBNAIL_WIDTH, Constants.THUMBNAIL_HEIGHT));
        }
        return thumbnailList;
    }
}
