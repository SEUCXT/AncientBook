package com.seu.architecture.service.impl;

import com.google.common.collect.ImmutableList;
import com.seu.architecture.config.Constants;
import com.seu.architecture.model.*;
import com.seu.architecture.repository.BookSolrRepository;
import com.seu.architecture.repository.UserRepository;
import com.seu.architecture.service.IndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexingServiceSolrImpl implements IndexingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexingServiceSolrImpl.class);

    @Autowired
    TaskExecutor taskExecutor;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookSolrRepository bookSolrRepository;

    @Override
    public void submitIndexingBookJob(List<Book> books) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                indexingBooks(books);
            }
        });
    }

    @Override
    public void submitIndexingBookJob(Book book) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                indexingBooks(ImmutableList.of(book));
            }
        });
    }

    private void indexingBooks(List<Book> books) {
        bookSolrRepository.save(books);
        LOGGER.info(String.format("Indexing books, count=%d", books.size()));
    }


    @Override
    public void submitIndexingUsersJob(List<User> users) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                indexingUsers(users);
            }
        });
    }

    @Override
    public void submitIndexingUsersJob(User user) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                indexingUsers(ImmutableList.of(user));
            }
        });
    }

    private void indexingUsers(List<User> users) {
        userRepository.save(users);
        LOGGER.info(String.format("Indexing Users, count=%d", users.size()));
    }


    @Override
    public void submitIndexingImageJob(Book book, String imageType, List<String> imageUrlList) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                indexingImages(book, imageType, imageUrlList);
            }
        });

    }

    private void indexingImages(Book book, String imageType, List<String> imageUrlList) {
        if (book instanceof AncientBook) {
            AncientBook ancientBook = (AncientBook) book;
            if (imageType.equals(Constants.THUMBNAIL)) {
                ancientBook.setThumbnail(imageUrlList.get(0));
                ancientBook.setThumbnailList(imageUrlList);
            } else if (imageType.equals(Constants.ORIGINAL_IMAGE)) {
                ancientBook.setZhaopianbianhao(imageUrlList);
            }
            bookSolrRepository.save(ancientBook);
        }else if (book instanceof RepublicBook) {
            RepublicBook republicBook = (RepublicBook) book;
            if (imageType.equals(Constants.THUMBNAIL)) {
                republicBook.setThumbnail(imageUrlList.get(0));
                republicBook.setThumbnailList(imageUrlList);
            } else if (imageType.equals(Constants.ORIGINAL_IMAGE)) {
                republicBook.setZhaopianbianhao(imageUrlList);
            }
            bookSolrRepository.save(republicBook);
        }else if (book instanceof CharacteristicBook) {
            CharacteristicBook characteristicBook = (CharacteristicBook) book;
            if (imageType.equals(Constants.THUMBNAIL)) {
                characteristicBook.setThumbnail(imageUrlList.get(0));
                characteristicBook.setThumbnailList(imageUrlList);
            } else if (imageType.equals(Constants.ORIGINAL_IMAGE)) {
                characteristicBook.setZhaopianbianhao(imageUrlList);
            }
            bookSolrRepository.save(characteristicBook);
        }
        LOGGER.info(String.format("Indexing " + book.getId() + "Image, count=%d", imageUrlList.size()));
    }
}
