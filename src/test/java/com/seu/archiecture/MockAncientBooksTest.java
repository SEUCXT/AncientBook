package com.seu.archiecture;

import com.seu.architecture.ArchitectureApplication;
import com.seu.architecture.config.Constants;
import com.seu.architecture.model.AncientBook;
import com.seu.architecture.model.Book;
import com.seu.architecture.service.BookService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.InputStream;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchitectureApplication.class)
public class MockAncientBooksTest {

    @Autowired
    BookService bookService;

    @Test
    public void insertMockAncientBooks() throws Exception {
        List<List<String>> file = readXls("AncientBook.xlsx");
        List<Book> books = new ArrayList<>();
        File imagePath = new File(Constants.ORIGIN_THUMBNAIL_DIR + '/' + "000277 Country Planning/");
        File imageNames[] = imagePath.listFiles();
        List<String> imageList = new ArrayList<>();
        for(int i = 0; i < imageNames.length; i++){
            imageList.add("thumbnail/" + imageNames[i].getName());
        }
        Random random = new Random();
        for (int index = 0; index < file.size(); ++index) {
            AncientBook book = new AncientBook();
            book.setTitle(file.get(index).get(0));
            book.setAuthor(file.get(index).get(1));
            book.setBeizhu(UUID.randomUUID().toString().substring(0, 12));
            book.setCangshubanben(UUID.randomUUID().toString().substring(0, 4));
            book.setChengshushijian(UUID.randomUUID().toString().substring(0, 4));
            book.setChuban(file.get(index).get(2));
            book.setChubanshijian(UUID.randomUUID().toString().substring(0, 4));
            book.setJuanshu(random.nextInt(48));
            //book.setZhaopianbianhao(imageList);
            books.add(book);
        }
        bookService.insertBooks(books);
    }

    List<List<String>> readXls(String path) throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<List<String>> result = new ArrayList<List<String>>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null)
                continue;
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                int minColIx = xssfRow.getFirstCellNum();
                int maxColIx = xssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<>();
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell == null)
                        continue;
                    rowList.add(cell.toString());
                }
                result.add(rowList);
            }
        }
        return result;
    }
}
