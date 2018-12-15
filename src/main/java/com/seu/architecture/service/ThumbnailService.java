package com.seu.architecture.service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by 17858 on 2017/5/11.
 */
public interface ThumbnailService {
    String thumbnailName(String fileName, int width, int height);

    void submitThumbnailJobs(List<String> fileNames);

    Future<?> submitThumbnailJob(String fileName);

    void thumbnail(String imageName, int width, int height);

    void thumbnailSave(String thumbnailsPath, String originImagePath, String imageName, int width, int height);

    String getThumbnailPath(String thumbnailsPath, String imagePath, String imageName, int width, int height);

    List<String> image2Thumbnails(String thumbnailsPath, String imageName, List<String> imageList);
}

