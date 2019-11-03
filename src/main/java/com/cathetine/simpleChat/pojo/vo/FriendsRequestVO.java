package com.cathetine.simpleChat.pojo.vo;

import lombok.Data;

/**
 * @Author:xjk
 * @Date 2019/11/2 10:26
 * 好友请求视图模型
 */
@Data
public class FriendsRequestVO {
    private String userId;
    private String thumbFaceImgUrl;
    private String userName;
    private String nickName;
}
