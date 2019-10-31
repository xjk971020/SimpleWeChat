package com.cathetine.simpleChat.controller;

import com.cathetine.simpleChat.pojo.Users;
import com.cathetine.simpleChat.pojo.bo.UsersBO;
import com.cathetine.simpleChat.response.CommonReturnType;
import com.cathetine.simpleChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:xjk
 * @Date 2019/10/31 21:36
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/setNickname")
    public CommonReturnType setNickname(@RequestBody UsersBO userBO){

        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setNickname(userBO.getNickname());

        userService.updateUserByUserId(user);

        return CommonReturnType.create(user);
    }
}
