package com.seu.architecture.controller;

import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.BookService;
import com.seu.architecture.service.BookSearchService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 17858 on 2017-10-24.
 */
@Controller
@RequestMapping("/searchBook")
public class BookSearchController {

    @Autowired
    BookSearchService bookSearchService;

    @Autowired
    BookService bookService;

    /**
     * 简单检索
     */
    @RequestMapping("/simpleSearch")
    @ResponseBody
    //@RequiresPermissions("book_search")
    public ResponseEntity<ViewObject> listAncientBooksBySimpleSearch(@RequestParam("searchTerm") String searchTerm,
                                                                     @RequestParam("pageNumber") int pageNumber,
                                                                     @RequestParam("pageSize") int pageSize,
                                                                     @RequestParam("sortDirections") String sortDirections,
                                                                     @RequestParam("sortProperties") String sortProperties,
                                                                     @RequestParam("type") String type,
                                                                     @RequestParam(value = "refinedAuthorFacets", defaultValue = "") String refinedAuthorFacets,
                                                                     @RequestParam(value = "refinedChubanFacets", defaultValue = "") String refinedChubanFacets) {
        ViewObject vo = new ViewObject();
        vo.set(ViewObject.ERROR, 0).set(ViewObject.DATA, bookSearchService.simpleSearch(
                searchTerm,
                pageNumber,
                pageSize,
                sortDirections,
                sortProperties,
                type,
                refinedAuthorFacets,
                refinedChubanFacets));
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/{id}")
    @ResponseBody
   // @RequiresPermissions("book_read")
    public ResponseEntity<ViewObject> getBookAllInfo(@PathVariable("id") String id) {
        ViewObject vo = new ViewObject();
        vo.set(ViewObject.ERROR, 0)
                .set(ViewObject.DATA, bookService.getBook(id));
        return ResponseEntity.ok(vo);
    }

    /**
     * 多字段检索
     */
    @RequestMapping("/multiPropertiesSearch")
    @ResponseBody
    //@RequiresPermissions("book_search")
    public ResponseEntity<ViewObject> listAncientBooksByAdvancedSearch(@RequestParam("properties") String properties,
                                                                       @RequestParam("values") String values,
                                                                       @RequestParam("pageNumber") int pageNumber,
                                                                       @RequestParam("pageSize") int pageSize,
                                                                       @RequestParam("sortDirections") String sortDirections,
                                                                       @RequestParam("sortProperties") String sortProperties,
                                                                       @RequestParam("type") String type,
                                                                       @RequestParam(value = "refinedAuthorFacets", defaultValue = "") String refinedAuthorFacets,
                                                                       @RequestParam(value = "refinedChubanFacets", defaultValue = "") String refinedChubanFacets) {
        ViewObject vo = new ViewObject();
        vo.set(ViewObject.ERROR, 0).set(ViewObject.DATA, bookSearchService.multiPropertiesSearch(
                properties,
                values,
                pageNumber,
                pageSize,
                sortDirections,
                sortProperties,
                type,
                refinedAuthorFacets,
                refinedChubanFacets));
        return ResponseEntity.ok(vo);
    }
}
