package com.cathetine.simpleChat.service;

import com.cathetine.simpleChat.netty.ChatMessage;
import com.cathetine.simpleChat.pojo.ChatMsg;
import com.cathetine.simpleChat.pojo.Users;
import com.cathetine.simpleChat.pojo.bo.UsersBO;
import com.cathetine.simpleChat.pojo.vo.FriendsRequestVO;
import com.cathetine.simpleChat.pojo.vo.MyFriendsVO;

import java.util.List;

/**
 * 用户相关操作接口
 */
public interface UserService {
    /**
     * 增加一个用户
     * @param user
     * @return
     * @throws Exception
     */
    int insertUser(Users user) throws Exception;

    /**
     * 修改昵称
     * @param usersBO
     * @return
     */
    Users setNickName(UsersBO usersBO);

    /**
     * 根据用户id修改用户信息
     * @param user
     * @return
     */
    int updateUserByUserId(Users user);

    /**
     * 通过用户名查找用户
     * @param userName
     * @return
     */
    Users findUserByUserName(String userName);

    /**
     * 修改用户的头像信息,包括高清图像和缩略图
     * @param usersBO
     * @return
     * @throws Exception
     */
    Users updateUsersFaceInfo(UsersBO usersBO) throws Exception;

    /**
     * 查找好友功能实现
     * @param userId
     * @param friendUserName
     * @return
     */
    Users searchFriendByUserName(String userId, String friendUserName);

    /**
     * 通过用户名查找某用户
     * @param userName
     * @return
     */
    Users queryUsersByUserName(String userName);

    /**
     * 查找某用户是否已经拥有某好友
     * @return
     */
    boolean queryFriendExists(Users user, Users friendUser);

    /**
     * 发送好友请求
     * @param userId
     * @param friendUserName
     */
    void sendFriendRequest(String userId, String friendUserName);

    /**
     * 通过userId查找好友请求
     * @param acceptUserId
     * @return
     */
    List<FriendsRequestVO> queryFriendRequestByUserId(String acceptUserId);

    /**
     * 拒绝或者接受好友请求
     * @param acceptUserId
     * @param endUserId
     * @param operateType
     * @return
     */
    List<MyFriendsVO> operateFriendRequest(String acceptUserId, String endUserId, String operateType);

    /**
     * 根据userId查询该用户拥有的好友
     * @param userId
     * @return
     */
    List<MyFriendsVO> queryFriendsByUserId(String userId);

    /**
     * 用户手机端获取未签收的消息列表
     * @param acceptUserId
     * @return
     */
    List<ChatMsg> getUnReadMsg(String acceptUserId);

    /**
     * 保存聊天信息
     * @param chatMsg
     * @return
     */
    String saveMsg(ChatMessage chatMsg);

    /**
     * 批量修改未读信息
     * @param msgIdList
     */
    void updateMsgSigned(List<String> msgIdList);


}
