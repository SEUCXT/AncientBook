package com.seu.architecture.repository;

import com.seu.architecture.model.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 17858 on 2018-12-03.
 */
@Repository
public interface CollectionRepository extends CrudRepository<Collection, Long> {

    List<Collection> findByUserId(String userId);

    Collection findByUserIdAndBookId(String userId, String bookId);

}
