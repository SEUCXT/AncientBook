package com.seu.architecture.repository;

import com.seu.architecture.model.AncientBook;
import com.seu.architecture.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BookSolrRepository extends SolrCrudRepository<Book, String> {

    Page<Book> findByTitleOrAuthor(String title, String author, Pageable page);

    Page<Book> findByTitleLike(String title, Pageable page);

    Page<Book> findByTitleStartsWith(String title, Pageable page);

    Page<Book> findByAuthorLike(String author, Pageable page);

    Page<Book> findByAuthorStartsWith(String author, Pageable pageable);

    Page<Book> findByChubanStartsWith(String chuban, Pageable pageable);

    Page<Book> findByChubanshijianBetween(Date from, Date to, Pageable pageable);
}
