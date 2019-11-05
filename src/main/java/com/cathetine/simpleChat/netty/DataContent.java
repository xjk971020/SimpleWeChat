package com.cathetine.simpleChat.netty;

import java.io.Serializable;

public class DataContent implements Serializable {

	private static final long serialVersionUID = 8021381444738260454L;

	private Integer action;		// 动作类型
	private ChatMessage chatMessage;	// 用户的聊天内容entity
	private String extand;		// 扩展字段
	
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public ChatMessage getChatMessage() {
		return chatMessage;
	}
	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}
	public String getExtand() {
		return extand;
	}
	public void setExtand(String extand) {
		this.extand = extand;
	}
}
