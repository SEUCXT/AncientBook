package com.seu.architecture.controller;

import com.seu.architecture.model.Book;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 17858 on 2017-11-14.
 */

@Controller
@RequestMapping(value = "/import")
public class ImportController {

    @Autowired
    SolrUpdateService solrUpdateService;

    @Autowired
    BookService bookService;

    @Autowired
    MetadataParsingService metadataParsingService;

    @Autowired
    DecompressService decompressService;

    @Autowired
    UploadService uploadService;

    @Autowired
    LocalService localService;
    /**
     * 上传压缩的图书元信息
     *
     * @param type
     * @param file
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/importDataCompressed")
    @ResponseBody
    public ResponseEntity<ViewObject> uploadCompressedFile(@RequestParam("type") String type, @RequestParam("file") MultipartFile file, HttpServletRequest request, ModelMap model) {
        ViewObject vo = new ViewObject();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        File dir = new File(path);
        File targetFile = new File(path, fileName);
        boolean flag = false;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //保存文件
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUrl", request.getContextPath() + "/upload/" + fileName);
        //解压
        List<String> pathList = new ArrayList<>();
        if (flag) {
            pathList = decompressService.decompress(targetFile, path);
        }
        //读取文件
        try {
            for (String s : pathList) {
                File readFile = new File(s);
                InputStream is = new FileInputStream(readFile);
                List<Book> books = metadataParsingService.parseBookXssf(type, is);
                bookService.insertBooks(books); //提交结果
            }
            vo.set(ViewObject.ERROR, 0);
        } catch (IOException e) {
            e.printStackTrace();
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return ResponseEntity.ok(vo);
    }
    /**
     * 上传单个图书信息的元数据
     *
     * @param type
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/importData", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ViewObject> importData(@RequestParam("type") String type,
                                                 @RequestParam("file") MultipartFile file) throws IOException {
        ViewObject vo = new ViewObject();
        try {
            List<Book> books = metadataParsingService.parseBookXssf(type, file.getInputStream());
            bookService.insertBooks(books);
            vo.set(ViewObject.ERROR, 0).set(ViewObject.MESSAGE, "书籍添加成功！");
        } catch (IOException e) {
            e.printStackTrace();
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return ResponseEntity.ok(vo);
    }

    /**
     *
     * @param type
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importImage")
    @ResponseBody
    public ResponseEntity<ViewObject> uploadCompressedImage(@RequestParam("type") String type,
                                                            @RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        ViewObject vo = uploadService.UploadCompressedImage(file.getInputStream(), path, fileName, type);
        return ResponseEntity.ok(vo);
    }

    @RequestMapping(value = "/importLoalImage")
    @ResponseBody
    public ResponseEntity<ViewObject> importLocalImage() throws Exception {
        ViewObject vo = localService.scanImagePath();
        return ResponseEntity.ok(vo);
    }


    @RequestMapping(value = "/solrUpdate")
    @ResponseBody
    public void solrUpdate() throws Exception {
        solrUpdateService.solrUpdate();

    }


}
