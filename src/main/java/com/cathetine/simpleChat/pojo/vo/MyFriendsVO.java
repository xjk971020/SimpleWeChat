package com.cathetine.simpleChat.pojo.vo;

import lombok.Data;
import org.apache.shiro.SecurityUtils;

/**
 * @Author:xjk
 * @Date 2019/11/2 10:42
 * 好友列表里的好友视图模型
 */
@Data
public class MyFriendsVO {
    private String friendUserId;
    private String friendUserName;
    private String friendUserFaceImg;
    private String friendUserNickName;
}
