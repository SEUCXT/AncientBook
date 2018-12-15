package com.seu.architecture.service;

import com.seu.architecture.repository.RecommendBookResponse;

import java.util.List;

/**
 * Created by 17858 on 2018-12-06.
 */
public interface RecommendService {

    List<RecommendBookResponse> getRecommendResult(String type, int pageNum, int pageSize);

}
