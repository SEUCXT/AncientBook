package com.seu.archiecture;

import com.seu.architecture.ArchitectureApplication;
import com.seu.architecture.model.Book;
import com.seu.architecture.model.AncientBook;
import com.seu.architecture.repository.BookPlusRepository;
import com.seu.architecture.service.BookSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

/**
 * Created by 17858 on 2017/4/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchitectureApplication.class)
public class AncientBooksSolrQueryTest {
    @Autowired
    BookSearchService bookSearchService;

    @Autowired
    BookPlusRepository bookPlusRepository;



    private void displayQueryResult(Page<Book> ancientBooks) {
        System.out.println("Searching Result:");
        Iterator<Book> it = ancientBooks.iterator();
        while (it.hasNext()) {
            Book current = it.next();
            System.out.println(current.getTitle() + " " + current.getAuthor() + " " + current.getChuban());
        }
    }



    private void displayQueryAllBookResult(Page<Book> allBooks) {
        System.out.println("Searching Result:");
        Iterator<Book> it = allBooks.iterator();
        while (it.hasNext()) {
            Book current = it.next();
            System.out.println(current.getTitle() + " " + current.getAuthor() + " " + current.getChuban());
        }
    }
}
