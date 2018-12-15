package com.seu.architecture.repository;

import com.seu.architecture.model.Book;

/**
 * Created by 17858 on 2018-12-06.
 */
public class RecommendBookResponse<T> {

    private T t;

    private Book book;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
