package com.seu.architecture.service.impl;

import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by 17858 on 2017-10-29.
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    DecompressService decompressService;

    @Autowired
    FastDFSService fastDFSService;

    @Autowired
    ThumbnailService thumbnailService;

    @Autowired
    PdfService pdfService;

    @Override
    public ViewObject UploadCompressedImage(InputStream inputStream, String path, String fileName, String bookType) {
        ViewObject vo = new ViewObject();
        int dotPos = fileName.lastIndexOf(".");
        //String extName = fileName.substring(dotPos + 1);
        int bookid = Integer.parseInt(fileName.substring(0, 6));
        String bookName = fileName.substring(7, dotPos);
        File dir = new File(path);
        File targetFile = new File(path, fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //保存到本地
        try {
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
        } catch (Exception e) {
            e.printStackTrace();
            return vo.set(ViewObject.ERROR, "文件保存到本地出现错误");
        }
        //解压
        List<String> imageList = decompressService.decompress(targetFile, path);

        //判断是PDF还是jpg
        int dotPos1 = imageList.get(0).lastIndexOf(".");
        String extName = imageList.get(0).substring(dotPos1 + 1);
        if(extName.equals("pdf")){
            File pdfToImegeFileDir = new File(path, "padToImage");
            if(!pdfToImegeFileDir.exists()){
                pdfToImegeFileDir.mkdirs();
            }
            imageList = pdfService.pdfToImageList(imageList,pdfToImegeFileDir.toString());
        }


        //上传原始图片到FastDFS
        if (!imageList.isEmpty()) {
            vo = fastDFSService.uploadImageByBook(imageList, bookType, "OriginalImage", bookName, new Long(bookid));
            if ((int) vo.get(ViewObject.ERROR) == 1) {
                return vo;
            }
        }

        List<String> thumbnailList = thumbnailService.image2Thumbnails(path, bookName, imageList);
        if (!thumbnailList.isEmpty()) {
            vo = fastDFSService.uploadImageByBook(thumbnailList, bookType, "Thumbnail", bookName, new Long(bookid));
            if ((int) vo.get(ViewObject.ERROR) == 1) {
                return vo;
            }
        }
        deleteDir(new File(path));
        return vo;
    }


    /**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
