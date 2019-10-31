package com.cathetine.simpleChat.pojo;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "chat_msg")
public class ChatMsg {
    @Id
    private String id;

    @Column(name = "send_user_id")
    private String sendUserId;

    @Column(name = "accept_user_id")
    private String acceptUserId;

    private String msg;

    /**
     * 消息是否签收状态
     * 1：签收
     * 0：未签收
     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    /**
     * 发送请求的事件
     */
    @Column(name = "create_time")
    private Date createTime;

}