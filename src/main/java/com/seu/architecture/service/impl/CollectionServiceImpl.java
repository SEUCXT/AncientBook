package com.seu.architecture.service.impl;

import com.seu.architecture.model.Book;
import com.seu.architecture.model.Collection;
import com.seu.architecture.model.CollectionResponse;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.repository.CollectionRepository;
import com.seu.architecture.service.BookService;
import com.seu.architecture.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 17858 on 2018-12-03.
 */
@Service
public class CollectionServiceImpl implements CollectionService{

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private BookService bookService;

    @Override
    public List<CollectionResponse> getCollectionByUserId(String userId) {
        List<CollectionResponse> responseList = new ArrayList<>();
        List<Collection> collectionList = collectionRepository.findByUserId(userId);
        Collections.sort(collectionList, new Comparator<Collection>() {
            @Override
            public int compare(Collection o1, Collection o2) {
                if (o2.getTime().before(o1.getTime())) {
                    return -1;
                } else return 1;
            }
        });

        for (Collection collection : collectionList) {
            CollectionResponse response = new CollectionResponse();
            Book book = bookService.getBook(collection.getBookId());
            response.setBook(book);
            response.setCollection(collection);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public ViewObject addCollection(String userId, String bookId) {
        ViewObject vo = new ViewObject();
        Collection oldCollection = collectionRepository.findByUserIdAndBookId(userId, bookId);
        if (null != oldCollection) {
            vo.set(ViewObject.ERROR, 1).set(ViewObject.MESSAGE, "已收藏！");
            return vo;
        }
        Collection newCollection = new Collection();
        newCollection.setUserId(userId);
        newCollection.setBookId(bookId);
        newCollection.setTime(new Date());
        collectionRepository.save(newCollection);
        vo.set(ViewObject.ERROR, 0).set(ViewObject.MESSAGE, "收藏成功！");
        return vo;
    }

    @Override
    public void deleteCollection(String userId, String bookId) {
        Collection collection = collectionRepository.findByUserIdAndBookId(userId, bookId);
        if (null != collection) {
            collectionRepository.delete(collection);
        }
    }

}
