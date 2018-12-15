package com.seu.architecture.service;

import com.seu.architecture.model.CollectionResponse;
import com.seu.architecture.model.ViewObject;

import java.util.List;

/**
 * Created by 17858 on 2018-12-03.
 */
public interface CollectionService {

    List<CollectionResponse> getCollectionByUserId(String userId);

    ViewObject addCollection(String userId, String bookId);

    void deleteCollection(String userId, String bookId);

}
