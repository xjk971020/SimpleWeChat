package com.cathetine.simpleChat.controller;

import com.cathetine.simpleChat.pojo.Users;
import com.cathetine.simpleChat.response.CommonReturnType;
import com.cathetine.simpleChat.response.error.BussinessException;
import com.cathetine.simpleChat.response.error.EmBusinessError;
import com.cathetine.simpleChat.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:xjk
 * @Date 2019/10/31 11:05
 */
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/regisrerORLogin")
    public CommonReturnType registerOrLogin(@RequestBody Users user) throws Exception {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            throw new BussinessException(EmBusinessError.PARMETER_VALIDATION_ERROR,"用户名或者密码不能为空");
        }
        Users findUser = userService.findUserByUserName(user.getUsername());
        //没有查询到用户，则进行注册
        if (findUser == null) {
            if (userService.insertUser(user) > 0) {
                return CommonReturnType.create(user);
            } else {
                return CommonReturnType.create("创建用户" + user.getUsername() + "失败");
            }
        }
        //如果查询到用户，则通过shiro进行密码的校验
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(),user.getPassword());
        try {
            subject.login(usernamePasswordToken);
        }  catch (IncorrectCredentialsException e) {
            throw new BussinessException(EmBusinessError.USER_CREDENTIAL_ERROR);
        } catch (ExcessiveAttemptsException e) {
            throw new BussinessException(EmBusinessError.USER_LOGIN_ERROR_OVER);
        }
        return CommonReturnType.create(findUser);
    }

}
