package com.seu.architecture.model;

/**
 * Created by 17858 on 2018-12-04.
 */
public class BrowsingHistoryResponse {

    private BrowsingHistory history;

    private Book book;

    public BrowsingHistory getHistory() {
        return history;
    }

    public void setHistory(BrowsingHistory history) {
        this.history = history;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
