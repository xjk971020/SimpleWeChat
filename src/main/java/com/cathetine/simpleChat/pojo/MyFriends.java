package com.cathetine.simpleChat.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "my_friends")
public class MyFriends {
    @Id
    private String id;

    /**
     * 用户id
     */
    @Column(name = "my_user_id")
    private String myUserId;

    /**
     * 用户的好友id
     */
    @Column(name = "my_friend_user_id")
    private String myFriendUserId;
}