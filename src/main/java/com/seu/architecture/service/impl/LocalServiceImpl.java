package com.seu.architecture.service.impl;

import com.google.common.base.Strings;
import com.seu.architecture.config.Constants;
import com.seu.architecture.model.*;
import com.seu.architecture.repository.BookRepository;
import com.seu.architecture.repository.DataPathRepository;
import com.seu.architecture.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * Created by 17858 on 2017-11-19.
 */
@Service
public class LocalServiceImpl implements LocalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalServiceImpl.class);

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PdfService pdfService;

    @Autowired
    ThumbnailService thumbnailService;

    @Autowired
    MetadataParsingService metadataParsingService;

    @Autowired
    BookService bookService;

    @Autowired
    DataPathRepository dataPathRepository;

    @Override
    public ViewObject scanImagePath() {
        ViewObject vo = new ViewObject();

        Map<Long, String> map = getBookImageFolderPath();
        Iterable<Book> books = bookRepository.findAll();
        Iterator<Book> bookIterator = books.iterator();
        while (bookIterator.hasNext()) {
            Book book = bookIterator.next();
            if (Strings.isNullOrEmpty(book.getThumbnail())) {
                List<String> imgDestPath = new ArrayList<>();
                DataPath imgDataPath = dataPathRepository.findByType("img_path");
                if (imgDataPath == null) {
                    LOGGER.info("Can not find original image path!");
                    vo.set(ViewObject.ERROR, 1).set(ViewObject.MESSAGE, "Can not find original image path!");
                    return vo;
                }
                String imageSavePath = imgDataPath.getPath();
                //String imageSavePath = Constants.IMG_PATH;
                File imageSaveFile = new File(imageSavePath);
                if (!imageSaveFile.exists()) {
                    imageSaveFile.mkdirs();
                }

                Long bookId = book.getClientId();
                String imagePath = map.get(bookId);
                if(imagePath == null){
                    continue;
                }
                File imageFile = new File(imagePath);
                File[] imageListForOneBook = imageFile.listFiles();

                //创建每本书的图片保存文件夹
                String bookImagePath = imageSavePath + "\\" + String.valueOf(bookId);
                File bookImageFile = new File(bookImagePath);
                if (!bookImageFile.exists()) {
                    bookImageFile.mkdirs();
                }

                for (int i = 0; i < imageListForOneBook.length - 1; i++) {
                    String tmp = imageListForOneBook[i].getName();
                    int dotPos = tmp.lastIndexOf(".");
                    String extName = tmp.substring(dotPos + 1);
                    if (Constants.isFileAllowed(extName)) {      //判断是不是支持的图片格式
                        try {
                            InputStream in = new FileInputStream(imageListForOneBook[i].getAbsoluteFile());
                            String savePath = bookImagePath + "\\" + imageListForOneBook[i].getName();
                            OutputStream out = new FileOutputStream(savePath);
                            byte[] bytes = new byte[1024];
                            int len = -1;
                            while ((len = in.read(bytes)) != -1) {
                                out.write(bytes, 0, len);
                            }
                            imgDestPath.add(savePath);
                            in.close();
                            out.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (extName.equals("pdf")) {          //pdf
                        imgDestPath.add(pdfService.pdfToImage(imageListForOneBook[i].getAbsolutePath(), bookImagePath, i + 1));
                    } else if (extName.equals("tif") || extName.equals("tiff")) {
                        imgDestPath.add(tif2Image(imageListForOneBook[i], bookImagePath + "\\" + i + ".jpg"));
                    } else {
                        LOGGER.info("不支持图片类型！");
                        vo.set(ViewObject.ERROR, 1).set(ViewObject.MESSAGE, "包含不支持的图片类型!");
                        continue;
                    }
                }

                //转thumbnail
                List<String> thumbnailList = new ArrayList<>();
                String thumbnailPath = dataPathRepository.findByType("thumbnail_path").getPath() + "\\" + String.valueOf(bookId);
//                String thumbnailPath = Constants.THUMBNAIL_PATH + "\\" + String.valueOf(bookId);
                File thumbnailFile = new File(thumbnailPath);
                if (!thumbnailFile.exists()) {
                    thumbnailFile.mkdirs();
                }
                thumbnailList = thumbnailService.image2Thumbnails(thumbnailPath, String.valueOf(bookId), imgDestPath);

                String domain = "http:\\\\localhost\\";
                List<String> originalImgList = new ArrayList<>();
                for (int i = 0; i < imgDestPath.size(); i++) {
                    String httpUrl = imgDestPath.get(i).substring(3);
                    originalImgList.add(domain + httpUrl);
                }

                List<String> thumbnailImgList = new ArrayList<>();
                for (int i = 0; i < thumbnailList.size(); i++) {
                    String httpUrl = thumbnailList.get(i).substring(3);
                    thumbnailImgList.add(domain + httpUrl);
                }

                if (book instanceof AncientBook) {
                    AncientBook ancientBook = (AncientBook) book;
                    ancientBook.setThumbnail(thumbnailImgList.get(0));
                    ancientBook.setZhaopianbianhao(originalImgList);
                    ancientBook.setThumbnailList(thumbnailImgList);
                    bookRepository.save(ancientBook);

                } else if (book instanceof RepublicBook) {
                    RepublicBook republicBook = (RepublicBook) book;
                    republicBook.setThumbnail(thumbnailImgList.get(0));
                    republicBook.setZhaopianbianhao(originalImgList);
                    republicBook.setThumbnailList(thumbnailImgList);
                    bookRepository.save(republicBook);
                }
                if (book instanceof CharacteristicBook) {
                    CharacteristicBook characteristicBook = (CharacteristicBook) book;
                    characteristicBook.setThumbnail(thumbnailImgList.get(0));
                    characteristicBook.setZhaopianbianhao(originalImgList);
                    characteristicBook.setThumbnailList(thumbnailImgList);
                    bookRepository.save(characteristicBook);
                }
            }
        }
        vo.set(ViewObject.ERROR, 0)
                .set(ViewObject.MESSAGE, "导入图片成功！");
        return vo;
    }

    private Map<Long, String> getBookImageFolderPath() {
        DataPath imagePath = dataPathRepository.findByType("original_image_path");
        String[] imagePaths = imagePath.getPath().split(";");
        Map<Long, String> map = new HashMap<>();
        for (int k = 0; k < imagePaths.length; k++) {
            File originPath = new File(imagePaths[k]); //图片或者PDF文件夹
            File bookImageList[] = originPath.listFiles();
            for (int i = 0; i < bookImageList.length; i++) {
                String fileName = bookImageList[i].getName();
                String[] strs = fileName.split(" ");
                int bookId = Integer.parseInt(strs[0]);
                map.put(new Long(bookId), bookImageList[i].getAbsolutePath());
            }
        }
        return map;
    }

    @Override
    public ViewObject scanDataPath() {
        ViewObject vo = new ViewObject();
        try {
            String fileName = dataPathRepository.findByType("data_path").getPath();
            InputStream is = new FileInputStream(new File(fileName));
            List<Book> books = metadataParsingService.parseBookXssf("republic_book", is);
            bookService.insertBooks(books);
            vo.set(ViewObject.ERROR, 0)
                    .set(ViewObject.MESSAGE, "导入数据成功！");
        } catch (IOException e) {
            e.printStackTrace();
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return vo;
    }

    public String tif2Image(File srcFilePath, String destFilePath) {
        try {
            final BufferedImage tif = ImageIO.read(srcFilePath);
            ImageIO.write(tif, "jpg", new File(destFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destFilePath;
    }
}
