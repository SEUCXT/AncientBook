package com.seu.architecture.service.impl;

import com.seu.architecture.config.Constants;
import com.seu.architecture.model.Book;
import com.seu.architecture.model.LikeInfo;
import com.seu.architecture.model.NewBookInfo;
import com.seu.architecture.model.PopularInfo;
import com.seu.architecture.repository.*;
import com.seu.architecture.service.RecommendService;
import com.seu.architecture.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17858 on 2018-12-06.
 */
@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private NewBookRepository newBookRepository;

    @Autowired
    private PopularRepository popularRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<RecommendBookResponse> getRecommendResult(String type, int pageNum, int pageSize) {
        List<RecommendBookResponse> responseList = new ArrayList<>();
        if (Constants.POPULAR_BOOK.equals(type)) {
            Page<PopularInfo> infoPage = popularRepository.findAll(PageUtils.buildPageRequest(pageNum, pageSize));
            for (PopularInfo info : infoPage) {
                RecommendBookResponse response = new RecommendBookResponse();
                String bookId = info.getBookId();
                Book book = bookRepository.findOne(bookId);
                response.setBook(book);
                response.setT(info);
                responseList.add(response);
            }
        } else if (Constants.NEW_BOOK.equals(type)) {
            Page<NewBookInfo> infoPage = newBookRepository.findAll(PageUtils.buildPageRequest(pageNum, pageSize));
            for (NewBookInfo info : infoPage) {
                RecommendBookResponse response = new RecommendBookResponse();
                String bookId = info.getBookId();
                Book book = bookRepository.findOne(bookId);
                response.setBook(book);
                response.setT(info);
                responseList.add(response);
            }

        } else if (Constants.LIKE_BOOK.equals(type)) {
            Page<LikeInfo> infoPage =likeRepository.findAll(PageUtils.buildPageRequest(pageNum, pageSize));
            for (LikeInfo info : infoPage) {
                RecommendBookResponse response = new RecommendBookResponse();
                String bookId = info.getBookId();
                Book book = bookRepository.findOne(bookId);
                response.setBook(book);
                response.setT(info);
                responseList.add(response);
            }
        }
        return responseList;
    }
}
