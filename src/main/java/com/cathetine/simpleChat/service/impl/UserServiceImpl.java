package com.cathetine.simpleChat.service.impl;

import com.cathetine.simpleChat.netty.ChatMessage;
import com.cathetine.simpleChat.netty.enums.MsgSignFlagEnum;
import com.cathetine.simpleChat.constant.FileConst;
import com.cathetine.simpleChat.mapper.ChatMsgMapper;
import com.cathetine.simpleChat.mapper.FriendsRequestMapper;
import com.cathetine.simpleChat.mapper.MyFriendsMapper;
import com.cathetine.simpleChat.mapper.UsersMapper;
import com.cathetine.simpleChat.pojo.ChatMsg;
import com.cathetine.simpleChat.pojo.FriendsRequest;
import com.cathetine.simpleChat.pojo.MyFriends;
import com.cathetine.simpleChat.pojo.Users;
import com.cathetine.simpleChat.pojo.bo.UsersBO;
import com.cathetine.simpleChat.pojo.vo.FriendsRequestVO;
import com.cathetine.simpleChat.pojo.vo.MyFriendsVO;
import com.cathetine.simpleChat.response.error.BusinessException;
import com.cathetine.simpleChat.response.error.EmBusinessError;
import com.cathetine.simpleChat.service.UserService;
import com.cathetine.simpleChat.utils.FastDFSClient;
import com.cathetine.simpleChat.utils.FileUtils;
import com.cathetine.simpleChat.utils.PasswordUtil;
import com.cathetine.simpleChat.utils.QRCodeUtils;
import org.n3r.idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @Author:xjk
 * @Date 2019/10/31 13:18
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private FastDFSClient fastDFSClient;

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public int insertUser(Users user) throws Exception {

        String userId = sid.nextShort();
        String qrCodePath = FileConst.QR_CODE_PATH + userId + "qrcode.png";
        File file = new File(qrCodePath);
        if (!file.getParentFile().exists()) {
            boolean flag = file.mkdirs();
            if (flag) {
                logger.info("创建文件夹 {} 成功", file.getParentFile().getAbsolutePath());
            }
        }
        QRCodeUtils.createQRCode(qrCodePath, "simpleChat_qrcode:" + user.getUsername());
        MultipartFile qrcodeFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = fastDFSClient.uploadQRCode(qrcodeFile);
        user.setId(userId);
        user.setNickname(user.getUsername());
        user.setQrcode(qrCodeUrl);
        user.setFaceImage("");
        user.setFaceImageBig("");
        PasswordUtil.encryptUser(user);
        logger.info(user.toString());
        return usersMapper.insertSelective(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users setNickName(UsersBO userBO) {
        Users user = usersMapper.selectByPrimaryKey(userBO.getUserId());
        user.setNickname(userBO.getNickname());
        updateUserByUserId(user);
        System.out.println(user);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public int updateUserByUserId(Users user) {
        return usersMapper.updateByPrimaryKeySelective(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users findUserByUserName(String userName) {
        Users user = new Users();
        user.setUsername(userName);
        return usersMapper.selectOne(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users updateUsersFaceInfo(UsersBO usersBO) throws Exception {
        String faceData = usersBO.getFaceData();
        String userFaceImgPath = FileConst.FACE_IMG_PATH + usersBO.getUserId() + ".jpg";
        FileUtils.base64ToFile(userFaceImgPath,faceData);
        MultipartFile faceFile = FileUtils.fileToMultipart(userFaceImgPath);
        String bigImgUrl = fastDFSClient.uploadBase64(faceFile);
        bigImgUrl = bigImgUrl.replace(FileConst.PIXEL_DOT,".");
        String[] faceUrls = bigImgUrl.split("\\.");
        String thumbImgUrl = faceUrls[0] + FileConst.PIXEL_DOT + faceUrls[1];
        Users users = usersMapper.selectByPrimaryKey(usersBO.getUserId());
        users.setFaceImageBig(bigImgUrl);
        users.setFaceImage(thumbImgUrl);
        int updateCount = usersMapper.updateByPrimaryKeySelective(users);
        System.out.println(users.toString());
        if (updateCount > 0) {
            return users;
        } else {
            logger.info("上传头像出现错误, 信息:{}",usersBO.toString());
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"上传头像出现错误");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users searchFriendByUserName(String userId, String friendUserName) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(EmBusinessError.PARMETER_VALIDATION_ERROR,"用户id不能为空");
        }
        if (StringUtils.isEmpty(friendUserName)) {
            throw new BusinessException(EmBusinessError.PARMETER_VALIDATION_ERROR,"搜索的用户名不能为空");
        }
        Users friendUser = queryUsersByUserName(friendUserName);
        if (friendUser == null) {
            throw new BusinessException(EmBusinessError.USER_SEARCH_ERROR, "查无此用户");
        }
        return friendUser;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUsersByUserName(String userName) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",userName);
        return usersMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public boolean queryFriendExists(Users user, Users friendUser) {
        Example example = new Example(MyFriends.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("myUserId",user.getId());
        criteria.andEqualTo("myFriendUserId",friendUser.getId());
        MyFriends myFriends = myFriendsMapper.selectOneByExample(example);
        return myFriends != null;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void sendFriendRequest(String userId, String friendUserName) {
        Users friendUser = queryUsersByUserName(friendUserName);
        Users user = usersMapper.selectByPrimaryKey(userId);
        if (friendUser.getId().equals(userId)) {
            throw new BusinessException(EmBusinessError.USER_SEARCH_ERROR,"不能添加你自己");
        }
        boolean friendExist = queryFriendExists(user, friendUser);
        if (friendExist) {
            throw new BusinessException(EmBusinessError.USER_SEARCH_ERROR,"该用户已经是您的好友");
        }
        Example example = new Example(FriendsRequest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId",userId);
        criteria.andEqualTo("acceptUserId",friendUser.getId());
        FriendsRequest friendsRequest = friendsRequestMapper.selectOneByExample(example);
        if (friendsRequest == null) {
            friendsRequest = new FriendsRequest();
            String requestId = sid.nextShort();
            friendsRequest.setId(requestId);
            friendsRequest.setAcceptUserId(friendUser.getId());
            friendsRequest.setSendUserId(userId);
            friendsRequest.setRequestDateTime(new Date());
            friendsRequestMapper.insertSelective(friendsRequest);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public List<FriendsRequestVO> queryFriendRequestByUserId(String acceptUserId) {
        if (StringUtils.isEmpty(acceptUserId)) {
            throw new BusinessException(EmBusinessError.PARMETER_VALIDATION_ERROR,"接收好友请求的用户id不能为空");
        }
        return usersMapper.queryFriendRequestList(acceptUserId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public List<MyFriendsVO> operateFriendRequest(String acceptUserId, String sendUserId, String operateType) {
        if (StringUtils.isEmpty(acceptUserId) || StringUtils.isEmpty(sendUserId) || StringUtils.isEmpty(operateType)) {
            throw new BusinessException(EmBusinessError.PARMETER_VALIDATION_ERROR,"缺少必须参数");
        }
        if ("接受".equals(operateType)) {

        } else if ("拒绝".equals(operateType)){
            Example example = new Example(FriendsRequest.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("sendUserId",sendUserId);
            criteria.andEqualTo("acceptUserId",acceptUserId);
            friendsRequestMapper.deleteByExample(example);
        }
        return queryFriendsByUserId(acceptUserId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<MyFriendsVO> queryFriendsByUserId(String userId) {
        return usersMapper.queryFriendsByUserId(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ChatMsg> getUnReadMsg(String acceptUserId) {
        if (StringUtils.isEmpty(acceptUserId)) {
            throw new BusinessException(EmBusinessError.PARMETER_VALIDATION_ERROR,"acceptUserId不能为空");
        }
        Example example = new Example(ChatMsg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("acceptUserId", acceptUserId);
        criteria.andEqualTo("signFlag", 0);
        List<ChatMsg> unReadMsgList = chatMsgMapper.selectByExample(criteria);
        return unReadMsgList;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String saveMsg(ChatMessage chatMessage) {
        ChatMsg msgDB = new ChatMsg();
        String msgId = sid.nextShort();
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMessage.getReceiverId());
        msgDB.setSendUserId(chatMessage.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
        msgDB.setMsg(chatMessage.getMsg());

        chatMsgMapper.insert(msgDB);

        return msgId;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        usersMapper.batchUpdateMsgSigned(msgIdList);
    }
}
