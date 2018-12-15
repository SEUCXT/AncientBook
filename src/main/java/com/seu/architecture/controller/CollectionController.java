package com.seu.architecture.controller;

import com.seu.architecture.model.Collection;
import com.seu.architecture.model.CollectionResponse;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by 17858 on 2018-12-03.
 */
@Controller
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<ViewObject> addCollection(@RequestParam("userId") String userId,
                                                    @RequestParam("bookId") String bookId) {
        ViewObject vo = collectionService.addCollection(userId, bookId);
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<ViewObject> deleteCollection(@RequestParam("userId") String userId,
                                                       @RequestParam("bookId") String bookId) {
        ViewObject vo = new ViewObject();
        collectionService.deleteCollection(userId, bookId);
        vo.set(ViewObject.ERROR, 0).set(ViewObject.MESSAGE, "移除收藏成功！");
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<ViewObject> getCollection(@RequestParam("userId") String userId) {
        ViewObject vo = new ViewObject();
        List<CollectionResponse> collectionList = collectionService.getCollectionByUserId(userId);
        vo.set(ViewObject.ERROR, 0).set(ViewObject.DATA, collectionList);
        return ResponseEntity.ok(vo);
    }
}
