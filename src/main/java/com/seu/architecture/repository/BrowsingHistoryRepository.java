package com.seu.architecture.repository;

import com.seu.architecture.model.BrowsingHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 17858 on 2018-12-04.
 */
@Repository
public interface BrowsingHistoryRepository extends CrudRepository<BrowsingHistory, Long> {

    List<BrowsingHistory> findByUserId(String userId);

}
