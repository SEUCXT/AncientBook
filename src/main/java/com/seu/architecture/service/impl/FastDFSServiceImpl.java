package com.seu.architecture.service.impl;

import com.seu.architecture.config.Constants;
import com.seu.architecture.model.*;
import com.seu.architecture.repository.BookRepository;
import com.seu.architecture.service.FastDFSService;
import com.seu.architecture.service.IndexingService;
import org.apache.commons.io.IOUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by 17858 on 2017-09-13.
 */
@Service
public class FastDFSServiceImpl implements FastDFSService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    IndexingService indexingService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FastDFSServiceImpl.class);

    private static TrackerClient trackerClient = null;
    private static TrackerServer trackerServer = null;
    private static StorageServer storageServer = null;
    private static StorageClient1 storageClient = null;


    @Override
    public String[] uploadImageToFastDFS(String path, String extName) {

        String[] strings = {};
        try {
            initConfigAndConnect();
            File file = new File(path);
            NameValuePair[] metaList = new NameValuePair[2];
            metaList[0] = new NameValuePair("fileName", file.getName());
            metaList[1] = new NameValuePair("bookName", " ");
            strings = storageClient.upload_file(path, extName, metaList);
            for (String string : strings) {
                System.out.println(string);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strings;
    }

    @Override
    public ViewObject uploadImageByBook(List<String> filePathList, String bookType, String imageType, String bookName, Long bookid) {
        ViewObject vo = new ViewObject();
        Book book = bookRepository.findByClientId(bookid);
        if (book == null) {
            vo.set(ViewObject.ERROR, 1).set(ViewObject.MESSAGE, "找不到对应的书籍信息！");
            return vo;
        }
        List<String> bookUrlList = new ArrayList<>();
        String[] strs;
        try {
            initConfigAndConnect();
            int page = 0;
            for (String path : filePathList) {
                File file = new File(path);
                NameValuePair[] metaList = new NameValuePair[4];
                metaList[0] = new NameValuePair("bookid", String.valueOf(bookid));
                metaList[1] = new NameValuePair("bookName", bookName);
                metaList[2] = new NameValuePair("imageType", imageType);
                metaList[3] = new NameValuePair("pageNum", String.valueOf(page++));
                String fileName = file.getName();
                int dotPos = fileName.lastIndexOf(".");
                String extName = fileName.substring(dotPos + 1);  //rar
                strs = storageClient.upload_file(path, extName, metaList);
                String res = Constants.IMAGE_URL + strs[0] + '/' + strs[1];
                bookUrlList.add(res);
            }
            vo.set(ViewObject.ERROR, 0).set(ViewObject.MESSAGE, "图片上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        if (book instanceof AncientBook) {
            AncientBook ancientBook = (AncientBook) book;
            if (imageType.equals("Thumbnail")) {
                ancientBook.setThumbnailList(bookUrlList);
                ancientBook.setThumbnail(bookUrlList.get(0));
                bookRepository.save(ancientBook);
            } else if (imageType.equals("OriginalImage")) {
                ancientBook.setZhaopianbianhao(bookUrlList);
                bookRepository.save(ancientBook);
            }
        }
        if (book instanceof RepublicBook) {
            RepublicBook republicBook = (RepublicBook) book;
            if (imageType.equals("Thumbnail")) {
                republicBook.setThumbnailList(bookUrlList);
                republicBook.setThumbnail(bookUrlList.get(0));
                bookRepository.save(republicBook);
            } else if (imageType.equals("OriginalImage")) {
                republicBook.setZhaopianbianhao(bookUrlList);
                bookRepository.save(republicBook);
            }
        }
        if (book instanceof CharacteristicBook) {
            CharacteristicBook characteristicBook = (CharacteristicBook) book;
            if (imageType.equals("Thumbnail")) {
                characteristicBook.setThumbnailList(bookUrlList);
                characteristicBook.setThumbnail(bookUrlList.get(0));
                bookRepository.save(characteristicBook);
            } else if (imageType.equals("OriginalImage")) {
                characteristicBook.setZhaopianbianhao(bookUrlList);
                bookRepository.save(characteristicBook);
            }
        }
        return vo;
    }

    @Override
    public void downloadImage(String groupName, String fileName) {
        try {
            initConfigAndConnect();
            byte[] b = storageClient.download_file(groupName, fileName);
            System.out.println(b);
            IOUtils.write(b, new FileOutputStream("E:/" + UUID.randomUUID().toString() + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteImage(String groupName, String fileName) {
        try {
            initConfigAndConnect();

            int state = storageClient.delete_file(groupName, fileName);
            if (state == 0) {
                LOGGER.info("文件删除成功！");
            } else {
                LOGGER.info("文件删除失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteImageTest(int num) {
        try {
            initConfigAndConnect();
            Book book = bookRepository.findByClientId(new Long(num));
            if (book instanceof AncientBook) {
                AncientBook ancientBook = (AncientBook) book;
                List<String> pathList = ancientBook.getZhaopianbianhao();
                for (int i = 0; i < pathList.size(); i++) {
                    storageClient.delete_file("group1", pathList.get(i).substring(7));
                    LOGGER.info("文件删除成功！");
                }

                List<String> pathList1 = ancientBook.getThumbnailList();
                for (int i = 0; i < pathList.size(); i++) {
                    storageClient.delete_file("group1", pathList1.get(i).substring(7));
                    LOGGER.info("文件删除成功！");
                }
            }else if (book instanceof RepublicBook) {
                RepublicBook republicBook = (RepublicBook) book;
                List<String> pathList = republicBook.getZhaopianbianhao();
                for (int i = 0; i < pathList.size(); i++) {
                    storageClient.delete_file("group1", pathList.get(i).substring(7));
                    LOGGER.info("文件删除成功！");
                }

                List<String> pathList1 = republicBook.getThumbnailList();
                for (int i = 0; i < pathList.size(); i++) {
                    storageClient.delete_file("group1", pathList1.get(i).substring(7));
                    LOGGER.info("文件删除成功！");
                }

            }else if (book instanceof CharacteristicBook) {
                CharacteristicBook characteristicBook = (CharacteristicBook) book;
                List<String> pathList = characteristicBook.getZhaopianbianhao();
                for (int i = 0; i < pathList.size(); i++) {
                    storageClient.delete_file("group1", pathList.get(i).substring(7));
                    LOGGER.info("文件删除成功！");
                }

                List<String> pathList1 = characteristicBook.getThumbnailList();
                for (int i = 0; i < pathList.size(); i++) {
                    storageClient.delete_file("group1", pathList1.get(i).substring(7));
                    LOGGER.info("文件删除成功！");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initConfigAndConnect() throws Exception {
        //初始化配置文件
        //ClientGlobal.init(FDFS_CLIENT_FILENAME);
        /** * 1.读取fastDFS客户端配置文件 */
        Resource resource = new ClassPathResource("fdfs_client.conf");
        File file = resource.getFile();
        String configFile = file.getAbsolutePath();
        /** * 2.配置文件的初始化信息 */
        ClientGlobal.init(configFile);

        //连接FastDFS，创建tracker和storage
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getConnection();
        storageClient = new StorageClient1(trackerServer, storageServer);
    }
}
