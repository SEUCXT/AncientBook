package com.seu.architecture.repository;

import com.seu.architecture.model.NewBookInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by 17858 on 2018-12-06.
 */
@Repository
public interface NewBookRepository extends PagingAndSortingRepository<NewBookInfo, Long> {

}
