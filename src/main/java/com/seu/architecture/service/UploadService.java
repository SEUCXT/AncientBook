package com.seu.architecture.service;

import com.seu.architecture.model.ViewObject;

import java.io.InputStream;


/**
 * Created by 17858 on 2017-10-29.
 */
public interface UploadService {

    ViewObject UploadCompressedImage(InputStream inputStream, String path, String fileName, String bookType);
}
