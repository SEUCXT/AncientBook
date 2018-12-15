package com.seu.architecture.service;

import com.seu.architecture.model.*;

import java.util.List;

public interface IndexingService {

    void submitIndexingBookJob(List<Book> books);

    void submitIndexingBookJob(Book book);

    void submitIndexingUsersJob(User user);

    void submitIndexingUsersJob(List<User> users);

    void submitIndexingImageJob(Book book, String imageType, List<String> imageUrlList);
}
