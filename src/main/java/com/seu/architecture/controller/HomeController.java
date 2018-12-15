package com.seu.architecture.controller;

import com.seu.architecture.config.Constants;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.ImageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    ImageService imageService;

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    /**
     * 获取缩略图
     * @param name
     * @param response
     */
    @RequestMapping("/img/thumbnails/{name:.+}")
    //@RequiresPermissions("book-read")
    public void getImageThumbnails(@PathVariable("name") String name, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            InputStream is = imageService.getThumbnailsImageStream(name, Constants.THUMBNAIL_WIDTH, Constants.THUMBNAIL_HEIGHT);
            if (is == null) {
                is = imageService.getThumbnailsImageStream(Constants.IMAGE_NOT_FOUND, Constants.THUMBNAIL_WIDTH, Constants.THUMBNAIL_HEIGHT);
            }
            StreamUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error(String.format("读取图片错误 %s %s", name, e.getMessage()));
        }
    }

    /**
     * 获取图片
     * @param name
     * @param response
     */
    @RequestMapping("/img/{name:.+}")
    //@RequiresPermissions("book-read")
    public void getImage(@PathVariable("name") String name,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            InputStream is = imageService.getImageStream(name);
            if (is == null) {
                is = imageService.getThumbnailsImageStream(Constants.IMAGE_NOT_FOUND, Constants.THUMBNAIL_WIDTH, Constants.THUMBNAIL_HEIGHT);
            }
            StreamUtils.copy(is, response.getOutputStream());
        } catch (Exception e) {
            LOGGER.error(String.format("读取图片错误 %s %s", name, e.getMessage()));
        }
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    //@RequiresPermissions("book-download")
    public ResponseEntity<ViewObject> uploadImage(@RequestParam("imageId") String imageId,
                                                  @RequestParam("file") MultipartFile file) throws IOException {
        ViewObject vo = imageService.uploadImage(imageId, file);
        return ResponseEntity.ok(vo);
    }
}