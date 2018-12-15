package com.seu.architecture.repository;

import com.google.common.base.Splitter;
import com.seu.architecture.model.Book;
import com.seu.architecture.model.SearchableAllBook;
import com.seu.architecture.model.SearchableAncientBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by chenxiaotao on 2017/4/18.
 */
@Repository
public class BookPlusRepositoryImpl implements BookPlusRepository {

    private static final Set<String> containsProperties = new HashSet<String>() {
        {
            add(SearchableAncientBook.AUTHOR_FIELD_NAME);
            add(SearchableAncientBook.TITLE_FIELD_NAME);
            add(SearchableAncientBook.CHUBAN_FIELD_NAME);
            add(SearchableAncientBook.AUTHOR_NOSPLIT_FIELD_NAME);
            add(SearchableAncientBook.TITLE_NOSPLIT_FIELD_NAME);
            add(SearchableAncientBook.CHUBAN_NOSPLIT_FIELD_NAME);
        }
    };

    // 需要同时检查nosplit的字段
    private static final Map<String, String> nosplitProperties = new HashMap<String, String>() {
        {
            put(SearchableAncientBook.AUTHOR_FIELD_NAME, SearchableAncientBook.AUTHOR_NOSPLIT_FIELD_NAME);
            put(SearchableAncientBook.TITLE_FIELD_NAME, SearchableAncientBook.TITLE_NOSPLIT_FIELD_NAME);
            put(SearchableAncientBook.CHUBAN_FIELD_NAME, SearchableAncientBook.CHUBAN_NOSPLIT_FIELD_NAME);
        }
    };

    // TODO
    private static final Set<String> betweenProperties = new HashSet<String>() {
    };

    @Autowired
    SolrTemplate solrTemplate;

    @Override
    public Page<Book> multiPropertiesSearch(Map<String, List<String>> properties,
                                            Pageable pageable,
                                            String type,
                                            List<String> refinedAuthorFacets,
                                            List<String> refinedChubanFacets) {
        FacetAndHighlightQuery search = new SimpleFacetAndHighlightQuery(createMultiPropertiesConditions(properties), pageable);
        search.setFacetOptions(new FacetOptions()
                .addFacetOnField(SearchableAllBook.AUTHOR_NOSPLIT_FIELD_NAME)
                .addFacetOnField(SearchableAllBook.CHUBAN_NOSPLIT_FIELD_NAME)
                .addFacetOnField(SearchableAllBook.TITLE_NOSPLIT_FIELD_NAME));

        // Facet过滤
        Criteria filterConditions = createRefinedFacetsFilterConditions(refinedAuthorFacets, refinedChubanFacets);
        if (filterConditions != null) {
            FilterQuery filterQuery = new SimpleFilterQuery(filterConditions);
            search.addFilterQuery(filterQuery);
        }

        //过滤掉type
        if(!type.equals("")) {
            Criteria typeCondition = new Criteria(SearchableAllBook.TYPE_FIELD_NAME).contains(type);
            FilterQuery filterQuery = new SimpleFilterQuery(typeCondition);
            search.addFilterQuery(filterQuery);
        }

        return solrTemplate.queryForFacetAndHighlightPage(search, Book.class);
    }

    @Override
    public Page<Book> simpleSearch(String terms,
                                   String type,
                                   Pageable pageable,
                                   List<String> refinedAuthorFacets,
                                   List<String> refinedChubanFacets) {

        FacetAndHighlightQuery search = new SimpleFacetAndHighlightQuery(createSimpleSearchConditions(terms,type), pageable);
        search.setFacetOptions(new FacetOptions()
                .addFacetOnField(SearchableAllBook.AUTHOR_NOSPLIT_FIELD_NAME)
                .addFacetOnField(SearchableAllBook.CHUBAN_NOSPLIT_FIELD_NAME)
                .addFacetOnField(SearchableAllBook.TITLE_NOSPLIT_FIELD_NAME));

        // Facet过滤
        Criteria filterConditions = createRefinedFacetsFilterConditions(refinedAuthorFacets, refinedChubanFacets);
        if (filterConditions != null) {
            FilterQuery filterQuery = new SimpleFilterQuery(filterConditions);
            search.addFilterQuery(filterQuery);
        }

        //过滤掉type
        if(!type.equals("")) {
            Criteria typeCondition = new Criteria(SearchableAllBook.TYPE_FIELD_NAME).contains(type);
            FilterQuery filterQuery = new SimpleFilterQuery(typeCondition);
            search.addFilterQuery(filterQuery);
        }

        return solrTemplate.queryForFacetAndHighlightPage(search, Book.class);
    }



    private Criteria createRefinedFacetsFilterConditions( List<String> refinedAuthorFacets, List<String> refinedChubanFacets) {
        Criteria authorCondition = createSinglePropertyAndConditions(SearchableAllBook.AUTHOR_NOSPLIT_FIELD_NAME, refinedAuthorFacets);
        Criteria chubanCondition = createSinglePropertyAndConditions(SearchableAllBook.CHUBAN_NOSPLIT_FIELD_NAME, refinedChubanFacets);
        if (authorCondition == null) {
            return chubanCondition;
        }
        if (chubanCondition == null) {
            return authorCondition;
        }
        return authorCondition.and(chubanCondition);
    }

    private Criteria createSimpleSearchConditions(String terms,String type) {
        Criteria conditions = new Criteria();

        for (String word : Splitter.on(" ").split(terms)) {
            conditions = conditions.or(new Criteria(SearchableAncientBook.TITLE_FIELD_NAME).contains(word))
                                    .or(new Criteria(SearchableAllBook.AUTHOR_FIELD_NAME).contains(word))
                                    .or(new Criteria(SearchableAllBook.CHUBAN_FIELD_NAME).contains(word))
                                    .or(new Criteria(SearchableAllBook.AUTHOR_NOSPLIT_FIELD_NAME).contains(word))
                                    .or(new Criteria(SearchableAllBook.CHUBAN_NOSPLIT_FIELD_NAME).contains(word))
                                    .or(new Criteria(SearchableAllBook.TITLE_NOSPLIT_FIELD_NAME).contains(word));
        }
        return conditions;

    }

    private Criteria createMultiPropertiesConditions(Map<String, List<String>> properties) {
        Criteria multi = null;
        for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
            Criteria single = createSinglePropertyOrConditions(entry.getKey(), entry.getValue());
            if (multi == null) {
                multi = single;
            } else {
                multi = multi.and(single);
            }
        }
        return multi;
    }


    private Criteria createSinglePropertyAndConditions(String property, List<String> value) {
        if (value.size() == 0) {
            return null;
        }
        String values = value.get(0);
        // 字符串包含
        Criteria singlePropertyCondition = null;
        if (containsProperties.contains(property)) {
            for (String word : values.split(" ")) {
                if (singlePropertyCondition == null) {
                    singlePropertyCondition = new Criteria(property).contains(word);
                } else {
                    singlePropertyCondition = singlePropertyCondition
                            .and(new Criteria(property).contains(word));
                }
            }
            // 完整字段
            if (nosplitProperties.containsKey(property)) {
                String nosplitProperty = nosplitProperties.get(property);
                for (String word : values.split(" ")) {
                    if (singlePropertyCondition == null) {
                        singlePropertyCondition = new Criteria(nosplitProperty).contains(word);
                    } else {
                        singlePropertyCondition = singlePropertyCondition
                                .and(new Criteria(nosplitProperty).contains(word));
                    }
                }
            }
        } else if (betweenProperties.contains(property)) {  // TODO 时间范围检查

        }
        return singlePropertyCondition;
    }

    private Criteria createSinglePropertyOrConditions(String property, List<String> value) {
        if (value.size() == 0) {
            return null;
        }
        String values = value.get(0);
        // 字符串包含
        Criteria singlePropertyCondition = null;
        if (containsProperties.contains(property)) {
            for (String word : values.split(" ")) {
                if (singlePropertyCondition == null) {
                    singlePropertyCondition = new Criteria(property).contains(word);
                } else {
                    singlePropertyCondition = singlePropertyCondition
                            .or(new Criteria(property).contains(word));
                }
            }
            // 完整字段
            if (nosplitProperties.containsKey(property)) {
                String nosplitProperty = nosplitProperties.get(property);
                for (String word : values.split(" ")) {
                    if (singlePropertyCondition == null) {
                        singlePropertyCondition = new Criteria(nosplitProperty).contains(word);
                    } else {
                        singlePropertyCondition = singlePropertyCondition
                                .or(new Criteria(nosplitProperty).contains(word));
                    }
                }
            }
        } else if (betweenProperties.contains(property)) {  // TODO 时间范围检查

        }
        return singlePropertyCondition;
    }
}

