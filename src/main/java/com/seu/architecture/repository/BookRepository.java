package com.seu.architecture.repository;

import com.seu.architecture.model.AncientBook;
import com.seu.architecture.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, String> {

    Book findByClientId(Long clientId);

}
