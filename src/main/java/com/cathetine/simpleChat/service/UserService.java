package com.cathetine.simpleChat.service;

import com.cathetine.simpleChat.pojo.Users;

/**
 * 用户相关操作接口
 */
public interface UserService {
    int insertUser(Users user) throws Exception;
    int updateUserByUserId(Users user);
    Users findUserByUserName(String userName);
}
