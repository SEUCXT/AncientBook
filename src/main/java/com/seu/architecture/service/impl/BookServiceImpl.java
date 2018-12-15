package com.seu.architecture.service.impl;

import com.seu.architecture.model.Book;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.repository.BookRepository;
import com.seu.architecture.service.BookSearchService;
import com.seu.architecture.service.BookService;
import com.seu.architecture.service.IndexingService;
import com.seu.architecture.service.MetadataParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookSearchService bookSearchService;

    @Autowired
    IndexingService indexingService;

    @Autowired
    MetadataParsingService metadataParsingService;

    @Override
    public List<Book> insertBooks(List<Book> books) {
        bookRepository.save(books);
        indexingService.submitIndexingBookJob(books);
        return books;
    }

    @Override
    public Iterable<Book> listAncientBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBook(String id) {
        return bookRepository.findOne(id);
    }

    @Override
    public ViewObject parseAndInsertBook(InputStream inputStream) {
        ViewObject vo = new ViewObject();
        try {
            List<Book> books = metadataParsingService.parseAncientBookXssf(inputStream);
            bookRepository.save(books);
            indexingService.submitIndexingBookJob(books);
            vo.set(ViewObject.ERROR, 0)
                    .set(ViewObject.DATA, books);
        } catch (IOException e) {
            e.printStackTrace();
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return vo;
    }
}
