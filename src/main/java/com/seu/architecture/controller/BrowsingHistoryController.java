package com.seu.architecture.controller;

import com.seu.architecture.model.BrowsingHistory;
import com.seu.architecture.model.BrowsingHistoryResponse;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.BrowsingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by 17858 on 2018-12-04.
 */
@Controller
@RequestMapping("/browsingHistory")
public class BrowsingHistoryController {

    @Autowired
    private BrowsingHistoryService browsingHistoryService;

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<ViewObject> getBrowsingHistory(@RequestParam("userId") String userId) {
        ViewObject vo = new ViewObject();
        List<BrowsingHistoryResponse> historyList = browsingHistoryService.getBrowsingHistory(userId);
        vo.set(ViewObject.ERROR, 0).set(ViewObject.DATA, historyList);
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<ViewObject> addBrowsingHistory(@RequestParam("userId") String userId,
                                   @RequestParam("bookId") String bookId) {
        ViewObject vo = new ViewObject();
        browsingHistoryService.addBrowsingHistory(userId, bookId);
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<ViewObject> deleteBrowsingHistory(@RequestParam("historyId") long historyId,
                                                            @RequestParam("userId") String userId) {
        ViewObject vo = new ViewObject();
        browsingHistoryService.deleteBrowsingHistoryById(historyId);
        return ResponseEntity.ok(vo);
    }
}
