package com.seu.architecture.utils;

import org.springframework.data.domain.PageRequest;

/**
 * Created by 17858 on 2018-12-06.
 */
public class PageUtils {

    public static PageRequest buildPageRequest(int pageNumber, int pageSize) {
        return new PageRequest(pageNumber-1, pageSize, null);
    }
}
