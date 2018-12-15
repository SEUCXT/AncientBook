package com.seu.architecture.model;

import javax.persistence.*;

/**
 * Created by 17858 on 2018-12-06.
 */
@Entity
@Table(name = "popular_book")
public class PopularInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "book_id", nullable = false)
    String bookId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
