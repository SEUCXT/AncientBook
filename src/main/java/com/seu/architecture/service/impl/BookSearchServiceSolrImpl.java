package com.seu.architecture.service.impl;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.seu.architecture.model.Book;
import com.seu.architecture.repository.BookPlusRepository;
import com.seu.architecture.repository.BookSolrRepository;
import com.seu.architecture.service.BookSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.SolrPageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BookSearchServiceSolrImpl implements BookSearchService {

    static final String _2_LEVEL_SEPARATOR = ",";
    static final String _1_LEVEL_SEPARATOR = ";";
    static final String ASC = "asc";
    static final String DESC = "desc";
    private static final Logger LOGGER = LoggerFactory.getLogger(BookSearchServiceSolrImpl.class);

    @Autowired
    private BookSolrRepository repository;

    @Autowired
    private BookPlusRepository bookPlusRepository;

    @Override
    public Page<Book> simpleSearch(String searchTerm,
                                   int pageNumber,
                                   int pageSize,
                                   String sortDirections,
                                   String sortProperties,
                                   String type,
                                   String refinedAuthorFacets,
                                   String refinedChubanFacets) {
        Pageable pageable = createPage(pageNumber,
                pageSize,
                Splitter.on(_1_LEVEL_SEPARATOR).omitEmptyStrings().splitToList(sortDirections),
                Splitter.on(_1_LEVEL_SEPARATOR).omitEmptyStrings().splitToList(sortProperties));
        return bookPlusRepository.simpleSearch(searchTerm,type,
                pageable,
                Splitter.on(_1_LEVEL_SEPARATOR).omitEmptyStrings().splitToList(refinedAuthorFacets),
                Splitter.on(_1_LEVEL_SEPARATOR).omitEmptyStrings().splitToList(refinedChubanFacets));
    }

    @Override
    public Page<Book> multiPropertiesSearch(String properties,
                                            String values,
                                            int pageNumber,
                                            int pageSize,
                                            String sortDirections,
                                            String sortProperties,
                                            String type,
                                            String refinedAuthorFacets,
                                            String refinedChubanFacets) {
        List<String> ks = Splitter.on(_1_LEVEL_SEPARATOR).splitToList(properties);
        List<String> vs = Splitter.on(_1_LEVEL_SEPARATOR).splitToList(values);
        Map<String, List<String>> p = new HashMap<>();
        for (int i = 0; i < ks.size(); i++) {
            String property = ks.get(i);
            if (!Strings.isNullOrEmpty(vs.get(i))) {
                p.put(property, Splitter.on(_2_LEVEL_SEPARATOR).splitToList(vs.get(i)));
            }
        }
        Pageable pageable = createPage(pageNumber,
                pageSize,
                Splitter.on(_1_LEVEL_SEPARATOR).omitEmptyStrings().splitToList(sortDirections),
                Splitter.on(_1_LEVEL_SEPARATOR).omitEmptyStrings().splitToList(sortProperties));
        return bookPlusRepository.multiPropertiesSearch(p,
                pageable,
                type,
                Splitter.on(_1_LEVEL_SEPARATOR).omitEmptyStrings().splitToList(refinedAuthorFacets),
                Splitter.on(_1_LEVEL_SEPARATOR).omitEmptyStrings().splitToList(refinedChubanFacets));
    }

    private Pageable createPage(int pageNumber, int pageSize, List<String> sortDirections, List<String> sortProperties) {
        Sort sort = null;
        for (int i = 0; i < sortDirections.size(); i++) {
            String direction = sortDirections.get(i);
            String property = sortProperties.get(i);
            if (Strings.isNullOrEmpty(direction) || Strings.isNullOrEmpty(property)) { // 忽略掉空的排序属性
                continue;
            }
            Sort.Direction dir = Objects.equals(direction, ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
            if (sort == null) {
                sort = new Sort(dir, property);
            } else {
                sort = sort.and(new Sort(dir, property));
            }
        }
        return new SolrPageRequest(pageNumber, pageSize, sort);
    }
}
