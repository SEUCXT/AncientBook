package com.seu.architecture.controller;

import com.seu.architecture.model.ViewObject;
import com.seu.architecture.repository.RecommendBookResponse;
import com.seu.architecture.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by 17858 on 2018-12-06.
 */
@Controller
@RequestMapping("/recommendation")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @RequestMapping("/popular")
    @ResponseBody
    public ResponseEntity<ViewObject> getPopular(@RequestParam("pageNum") int pageNum,
                                                 @RequestParam("pageSize") int pageSize) {
        ViewObject vo = new ViewObject();
        List<RecommendBookResponse> responses= recommendService.getRecommendResult("POPULAR", pageNum, pageSize);
        vo.set(ViewObject.ERROR, 0).set(ViewObject.DATA, responses);
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/like")
    @ResponseBody
    public ResponseEntity<ViewObject> getLike(@RequestParam("pageNum") int pageNum,
                                              @RequestParam("pageSize") int pageSize) {
        ViewObject vo = new ViewObject();
        List<RecommendBookResponse> responses= recommendService.getRecommendResult("LIKE", pageNum, pageSize);
        vo.set(ViewObject.ERROR, 0).set(ViewObject.DATA, responses);
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/newBook")
    @ResponseBody
    public ResponseEntity<ViewObject> getNewBook(@RequestParam("pageNum") int pageNum,
                                                 @RequestParam("pageSize") int pageSize) {
        ViewObject vo = new ViewObject();
        List<RecommendBookResponse> responses= recommendService.getRecommendResult("NEW", pageNum, pageSize);
        vo.set(ViewObject.ERROR, 0).set(ViewObject.DATA, responses);
        return ResponseEntity.ok(vo);
    }
}
