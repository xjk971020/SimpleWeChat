package com.cathetine.simpleChat.controller;

import com.cathetine.simpleChat.pojo.Users;
import com.cathetine.simpleChat.pojo.bo.UsersBO;
import com.cathetine.simpleChat.response.CommonReturnType;
import com.cathetine.simpleChat.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:xjk
 * @Date 2019/10/31 21:36
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("设置用户昵称")
        @PostMapping("/update/nickname")
    public CommonReturnType setNickname(@RequestBody UsersBO userBO){
        return CommonReturnType.create(userService.setNickName(userBO));
    }

    @ApiOperation("上传base64格式的用户头像")
    @PostMapping("/upload/faceImg")
    public CommonReturnType updateBase64Face(@RequestBody UsersBO usersBO) throws Exception {
        Users users = userService.updateUsersFaceInfo(usersBO);
        return CommonReturnType.create(users);
    }

    @ApiOperation("根据用户名精确搜索用户信息")
    @PostMapping("/search/friend")
    public CommonReturnType searchFriend(String userId, String friendUserName) {
        Users friendUser = userService.searchFriendByUserName(userId,friendUserName);
        return CommonReturnType.create(friendUser);
    }

    @ApiOperation("发送好友请求")
    @PostMapping("/send/friendRequest")
    public CommonReturnType sendFriendRequest(String userId, String friendUserName) {
        userService.sendFriendRequest(userId, friendUserName);
        return CommonReturnType.create("发送好友请求成功");
    }

    @ApiOperation("根据接收好友请求的用户id查询好友请求")
    @GetMapping("/query/friendRequest/{acceptUserId}")
    public CommonReturnType queryFriendRequestByUserId(@PathVariable String acceptUserId) {
        return CommonReturnType.create(userService.queryFriendRequestByUserId(acceptUserId));
    }

    @ApiOperation("对好友请求进行操作")
    @PostMapping("/operate/friendRequest")
    public CommonReturnType operateFriendRequest(String acceptUserId, String sendUserId, String operateType) {
        return CommonReturnType.create(userService.operateFriendRequest(acceptUserId,sendUserId,operateType));
    }

    @ApiOperation("根据userId查询用户拥有的好友")
    @GetMapping("/query/friends/{userId}")
    public CommonReturnType queryMyFriends(@PathVariable String userId) {
        return CommonReturnType.create(userService.queryFriendsByUserId(userId));
    }

    @ApiOperation("用户手机端获取未签收的消息列表")
    @GetMapping("/query/unreadMsg/{acceptUserId}")
    public CommonReturnType getUnReadMsg(@PathVariable String acceptUserId) {
        System.out.println(acceptUserId + "----------->");
        return CommonReturnType.create(userService.getUnReadMsg(acceptUserId));
    }
}
