package com.cathetine.simpleChat.mapper;

import com.cathetine.simpleChat.pojo.Users;
import com.cathetine.simpleChat.pojo.vo.FriendsRequestVO;
import com.cathetine.simpleChat.pojo.vo.MyFriendsVO;
import com.cathetine.simpleChat.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersMapper extends MyMapper<Users> {
    List<FriendsRequestVO> queryFriendRequestList(@Param(value = "acceptUserId") String acceptUserId);
    List<MyFriendsVO> queryFriendsByUserId(@Param(value = "userId")String userId);
}