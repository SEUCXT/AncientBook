package com.seu.archiecture;

import com.seu.architecture.ArchitectureApplication;
import com.seu.architecture.model.AncientBook;
import com.seu.architecture.model.Book;
import com.seu.architecture.model.RepublicBook;
import com.seu.architecture.service.BookService;
import com.seu.architecture.service.MetadataParsingService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchitectureApplication.class)
public class MetadataParsingServiceTest {

    @Autowired
    MetadataParsingService metadataParsingService;

    @Autowired
    BookService bookService;

//    @Test
//    public void parseAncientBookXssfTest() throws Exception {
//        ClassLoader cl = Thread.currentThread().getContextClassLoader();
//        InputStream is = cl.getResourceAsStream("古籍登记表.xlsx");
//        AncientBook ancientBook = metadataParsingService.parseAncientBookXssf(is);
//        Assert.assertEquals("藝風堂文集 藝風堂文續集", ancientBook.getTitle());
//        Assert.assertEquals("繆荃孫", ancientBook.getAuthor());
//    }

    @Test
    public void parseRepublicBookHssfTest() throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream("民国图书登记表.xlsx");
        List<Book> republicBooks = metadataParsingService.parseRepublicBookXssf(is);
        bookService.insertBooks(republicBooks);
        for (Book book : republicBooks) {
            RepublicBook republicBook = (RepublicBook) book;
            System.out.println(republicBook.getClientId() + "" + republicBook.getAuthor() + " " + republicBook.getChuban());
        }
    }

    @Test
    public void parseAncientBookXssfTest() throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream("Ancientbook.xlsx");
        List<Book> ancientBookList = parseAncientBookXssf(is);
        bookService.insertBooks(ancientBookList);
    }


    public List<Book> parseAncientBookXssf(InputStream is) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return parseAncientBookSheet(sheet);
    }

    private List<Book> parseAncientBookSheet(Sheet sheet) {
        // FIXME 根据登记表读入
        List<Book> ancientBookList = new ArrayList<>();
        Random random = new Random();
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            AncientBook ancientBook = new AncientBook();
            ancientBook.setTitle(sheet.getRow(rowNum).getCell(0).toString());
            ancientBook.setAuthor(sheet.getRow(rowNum).getCell(1).toString());
            ancientBook.setChuban(sheet.getRow(rowNum).getCell(2).toString());

            ancientBookList.add(ancientBook);
        }
        return ancientBookList;
    }
}
