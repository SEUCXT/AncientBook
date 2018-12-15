package com.seu.architecture.service;

import com.seu.architecture.model.AncientBook;
import com.seu.architecture.model.Book;
import com.seu.architecture.model.ViewObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface BookService {

    Book getBook(String id);

    Iterable<Book> listAncientBooks();

    List<Book> insertBooks(List<Book> books);

    ViewObject parseAndInsertBook(InputStream inputStream);

}
