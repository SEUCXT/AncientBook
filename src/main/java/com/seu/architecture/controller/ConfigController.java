package com.seu.architecture.controller;

import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by 17858 on 2017-11-23.
 */
@Controller
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/configPermissions")
    @ResponseBody
    public ResponseEntity<ViewObject> configPermissions(@RequestParam("file") MultipartFile file) throws IOException{

        ViewObject vo = configService.configPermission(file.getInputStream());
        return ResponseEntity.ok(vo);

    }

    @RequestMapping(value = "/configRoles")
    @ResponseBody
    public ResponseEntity<ViewObject> configRoles(@RequestParam("file") MultipartFile file) throws IOException{
        ViewObject vo = configService.configRole(file.getInputStream());
        return ResponseEntity.ok(vo);
    }
}
