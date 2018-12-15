package com.seu.architecture.service.impl;

import com.seu.architecture.model.Book;
import com.seu.architecture.model.BrowsingHistory;
import com.seu.architecture.model.BrowsingHistoryResponse;
import com.seu.architecture.repository.BrowsingHistoryRepository;
import com.seu.architecture.service.BookService;
import com.seu.architecture.service.BrowsingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 17858 on 2018-12-04.
 */
@Service
public class BrowsingHistoryServiceImpl implements BrowsingHistoryService {

    @Autowired
    private BrowsingHistoryRepository browsingHistoryRepository;

    @Autowired
    private BookService bookService;

    @Override
    public List<BrowsingHistoryResponse> getBrowsingHistory(String userId) {
        List<BrowsingHistory> histories = browsingHistoryRepository.findByUserId(userId);
        Collections.sort(histories, new Comparator<BrowsingHistory>() {
            @Override
            public int compare(BrowsingHistory o1, BrowsingHistory o2) {
                if (o2.getTime().before(o1.getTime())) {
                     return -1;
                } else return 1;
            }
        });
        List<BrowsingHistoryResponse> responseList = new ArrayList<>();
        for (BrowsingHistory history : histories) {
            BrowsingHistoryResponse response = new BrowsingHistoryResponse();
            Book book = bookService.getBook(history.getBookId());
            response.setBook(book);
            response.setHistory(history);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public void addBrowsingHistory(String userId, String bookId) {
        BrowsingHistory browsingHistory = new BrowsingHistory();
        browsingHistory.setUserId(userId);
        browsingHistory.setBookId(bookId);
        browsingHistory.setTime(new Date());
        browsingHistoryRepository.save(browsingHistory);
    }

    @Override
    public void deleteBrowsingHistoryById(Long historyId) {
        browsingHistoryRepository.delete(historyId);
    }
}
