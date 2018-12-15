package com.seu.architecture.service;

import com.seu.architecture.model.BrowsingHistoryResponse;

import java.util.List;

/**
 * Created by 17858 on 2018-12-04.
 */
public interface BrowsingHistoryService {

    List<BrowsingHistoryResponse> getBrowsingHistory(String userId);

    void addBrowsingHistory(String userId, String bookId);

    void deleteBrowsingHistoryById(Long historyId);
}
