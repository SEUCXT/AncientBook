package com.seu.archiecture;

import com.seu.architecture.ArchitectureApplication;
import com.seu.architecture.config.Constants;
import com.seu.architecture.model.AncientBook;
import com.seu.architecture.repository.BookRepository;
import com.seu.architecture.service.BookService;
import com.seu.architecture.service.ThumbnailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17858 on 2017/5/15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchitectureApplication.class)
public class ThumbnailServiceTest {

    @Autowired
    ThumbnailService thumbnailService;

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository ancientBookRepository;

    @Test
    public void getThumbnailTest() {

        File originImagePath = new File(Constants.ORIGIN_IMAGE_DIR);
        File imageList[] = originImagePath.listFiles();
        for (int i = 0; i < imageList.length; i++) {
            //获取每本书文件夹，文件夹的名称是书名
            File currentBooks[] = imageList[i].listFiles();
            //创建缩略图文件夹
            File newFile = new File(Constants.ORIGIN_THUMBNAIL_DIR + '/' + imageList[i].getName());
            newFile.mkdirs();
            for (int j = 0; j < currentBooks.length; j++) {
                String imgName = currentBooks[j].getName();
                String bookImagePath = imageList[i].getAbsolutePath();
                String thumbnailsSavePath = newFile.toString();
                thumbnailService.thumbnailSave(thumbnailsSavePath + '/', bookImagePath + '/', imgName, Constants.THUMBNAIL_WIDTH, Constants.THUMBNAIL_HEIGHT);
            }
        }
    }

    @Test
    public void insertAncientBookThumbnail() {
        File imagePath = new File(Constants.ORIGIN_THUMBNAIL_DIR);
        File imageNames[] = imagePath.listFiles();
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < imageNames.length; i++) {
            imageList.add(imageNames[i].getName());
        }
        for (long i = 1; i <= 686; i++) {
            AncientBook ancientBook = (AncientBook) ancientBookRepository.findByClientId(i);
            ancientBook.setZhaopianbianhao(imageList);
            ancientBookRepository.save(ancientBook);
        }
    }

}
