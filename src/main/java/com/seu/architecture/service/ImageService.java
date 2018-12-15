package com.seu.architecture.service;

import com.seu.architecture.model.ViewObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface ImageService {

    ViewObject uploadImage(String imageId, MultipartFile multipartFile) throws IOException;

    InputStream getImageStream(String imageName);

    InputStream getThumbnailsImageStream(String imageName, int width, int height);
}

