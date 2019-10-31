package com.cathetine.simpleChat.service.impl;

import com.cathetine.simpleChat.constant.FileConst;
import com.cathetine.simpleChat.mapper.UsersMapper;
import com.cathetine.simpleChat.pojo.Users;
import com.cathetine.simpleChat.service.UserService;
import com.cathetine.simpleChat.utils.FastDFSClient;
import com.cathetine.simpleChat.utils.FileUtils;
import com.cathetine.simpleChat.utils.PasswordUtil;
import com.cathetine.simpleChat.utils.QRCodeUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Author:xjk
 * @Date 2019/10/31 13:18
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Override
    public int insertUser(Users user) throws Exception {

        String userId = sid.nextShort();
        String qrCodePath = FileConst.QR_CODE_PATH + userId + "qrcode.png";
        File file = new File(qrCodePath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
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
        return usersMapper.insertSelective(user);
    }

    @Override
    public int updateUserByUserId(Users user) {
        return usersMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Users findUserByUserName(String userName) {
        Users user = new Users();
        user.setUsername(userName);
        return usersMapper.selectOne(user);
    }
}
