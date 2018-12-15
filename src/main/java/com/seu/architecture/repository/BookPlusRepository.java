package com.seu.architecture.repository;

import com.seu.architecture.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by chenxiaotao on 2017/4/18.
 */
public interface BookPlusRepository {

    Page<Book> multiPropertiesSearch(Map<String, List<String>> properties,
                                     Pageable pageable,
                                     String type,
                                     List<String> refinedAuthorFacets,
                                     List<String> refinedChubanFacets);

    Page<Book> simpleSearch(String terms,
                            String type,
                            Pageable pageable,
                            List<String> refinedAuthorFacets,
                            List<String> refinedChubanFacets);
}

