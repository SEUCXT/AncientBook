package com.seu.architecture.controller;

import com.seu.architecture.model.UserRegisterRequest;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/loginManagement")
public class LoginController {

    @Autowired
    UserService userService;

//    /**
//     * 用户注册
//     * @param username
//     * @param password
//     * @return
//     */
//    @RequestMapping("/register")
//    @ResponseBody
//    public ResponseEntity<ViewObject> register(@RequestParam("username") String username,
//                                               @RequestParam("password") String password) {
//        ViewObject vo = userService.register(username, password);
//        System.out.println(username + password);
//        return ResponseEntity.ok(vo);
//    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ViewObject> register(@RequestBody UserRegisterRequest request) {
        ViewObject vo = userService.register(request);
        return ResponseEntity.ok(vo);
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @param remember
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResponseEntity<ViewObject> login(@RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            @RequestParam(value = "remember", defaultValue = "false") boolean remember) {
        ViewObject vo = userService.login(username, password, remember);
        return ResponseEntity.ok(vo);
    }

    /**
     * 退出登录
     * @param username
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ResponseEntity<ViewObject> logout(@RequestParam("username") String username){

        ViewObject vo = userService.logout(username);
        return ResponseEntity.ok(vo);
    }
}
