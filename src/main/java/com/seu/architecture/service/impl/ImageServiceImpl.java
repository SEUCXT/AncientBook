package com.seu.architecture.service.impl;

import com.seu.architecture.config.Constants;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
class ImageServiceImpl implements ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private ThumbnailServiceImpl thumbnailServiceImpl;

    @Override
    public ViewObject uploadImage(String imageId, MultipartFile file) throws IOException {
        ViewObject vo = new ViewObject();
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            vo.set(ViewObject.ERROR, 1);
            vo.set(ViewObject.MESSAGE, "后缀格式错误");
            return vo;
        }

        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!Constants.isFileAllowed(fileExt)) {
            vo.set(ViewObject.ERROR, 2);
            vo.set(ViewObject.MESSAGE, "后缀格式不允许");
            return vo;
        }

        String fileName = imageId + "." + fileExt;
        Files.copy(file.getInputStream(),
                new File(Constants.UPLOAD_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        vo.set(ViewObject.ERROR, 0);
        vo.set("name", fileName);
        thumbnailServiceImpl.submitThumbnailJob(fileName);
        return vo;
    }

    @Override
    public InputStream getImageStream(String name) {
        try {
            return new FileInputStream(Constants.UPLOAD_DIR + name);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("获取图片失败 %s %s ", name, e.getMessage()));
        }
        return null;
    }

    @Override
    public InputStream getThumbnailsImageStream(String name, int width, int height) {
        try {
            String fileName = Constants.UPLOAD_DIR + thumbnailServiceImpl.thumbnailName(name, width, height);
            File file = new File(fileName);
            if (!file.exists()) {
                thumbnailServiceImpl.thumbnail(name, width, height);
            }
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("获取图片失败 %s %s ", name, e.getMessage()));
        }
        return null;
    }
}
