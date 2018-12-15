package com.seu.architecture.service;

import com.seu.architecture.model.Book;
import org.springframework.data.domain.Page;

public interface BookSearchService {

    Page<Book> simpleSearch(String searchTerm,
                            int pageNumber,
                            int pageSize,
                            String sortDirections,
                            String sortProperties,
                            String type,
                            String refinedAuthorFacets,
                            String refinedChubanFacets);

    Page<Book> multiPropertiesSearch(String properties,
                                     String values,
                                     int pageNumber,
                                     int pageSize,
                                     String sortDirections,
                                     String sortProperties,
                                     String type,
                                     String refinedAuthorFacets,
                                     String refinedChubanFacets);
}
