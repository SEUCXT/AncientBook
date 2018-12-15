package com.seu.architecture.service;

import com.seu.architecture.model.ViewObject;

import java.util.List;

/**
 * Created by 17858 on 2017-09-13.
 */
public interface FastDFSService {

    String[] uploadImageToFastDFS(String path, String extName);

    ViewObject uploadImageByBook(List<String> filePathList, String bookType, String imageType, String bookName, Long bookID);

    void downloadImage(String groupName, String fileName);

    void deleteImage(String groupName, String fileName);

    void deleteImageTest(int num);
}
