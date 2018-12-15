package com.seu.archiecture;

import com.seu.architecture.ArchitectureApplication;
import com.seu.architecture.model.AncientBook;
import com.seu.architecture.model.Book;
import com.seu.architecture.repository.BookRepository;
import com.seu.architecture.service.FastDFSService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 17858 on 2017-09-13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchitectureApplication.class)
public class FastDFSTest {

    @Autowired
    FastDFSService fastDFSService;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void getFileNames() {
        String rootPath = "E:\\upload\\database\\000277 Country Planning";
        File originPath = new File(rootPath);
        File imageList[] = originPath.listFiles();
        for (int i = 0; i < imageList.length; i++) {
            //获取每本书文件夹，文件夹的名称是书名
            for (int j = 0; j < imageList.length; j++) {
                String imgName = imageList[j].getName();
                String bookImagePath = imageList[i].getAbsolutePath();
                System.out.println(imgName);
                System.out.println(bookImagePath);
            }
        }
    }

    @Test
    public void fastDFSUploadTest() {
        String path = "E:\\upload\\3.jpg";
        String extName = "jpg";
        String[] strings = fastDFSService.uploadImageToFastDFS(path, extName);
        for (String str : strings) {
            System.out.print(str + " ");
        }
        System.out.println();
    }

    @Test
    public void downloadFastDFSTest() {
        String groupName = "";
        String fileName = "";
        fastDFSService.deleteImage(groupName, fileName);
    }

    @Test
    public void deleteFastDFSTest() throws Exception {
        Book book = bookRepository.findByClientId(new Long(274));
        if (book instanceof AncientBook) {
            AncientBook ancientBook = (AncientBook) book;
            List<String> pathList = ancientBook.getThumbnailList();
            for (int i = 0; i < pathList.size(); i++) {
                fastDFSService.deleteImage("group1", pathList.get(i).substring(7));
            }
        }
    }

    public List<String> parsePathXssf(InputStream is) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return parsePathSheet(sheet);

    }

    private List<String> parsePathSheet(Sheet sheet) {
        // FIXME 根据登记表读入
        List<String> list = new ArrayList<>();
        Random random = new Random();
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            String str = sheet.getRow(rowNum).getCell(0).toString();
            list.add(str);
        }
        return list;
    }


    @Test
    public void copyImage() {
        AncientBook ancientBook = (AncientBook) bookRepository.findByClientId(1L);
        for (int i = 2; i < 11; i++) {
            AncientBook book = (AncientBook) bookRepository.findByClientId((long) i);
            book.setThumbnail(ancientBook.getThumbnail());
            List<String> list = ancientBook.getZhaopianbianhao();
            book.setZhaopianbianhao(list);
            List<String> list2 = ancientBook.getThumbnailList();
            book.setThumbnailList(list2);
            bookRepository.save(book);
        }
    }
}
