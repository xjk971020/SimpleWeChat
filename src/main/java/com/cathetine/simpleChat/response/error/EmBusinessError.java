package com.cathetine.simpleChat.response.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EmBusinessError implements CommonError {
    PARMETER_VALIDATION_ERROR(10001,"入参不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),

    USER_NOT_EXIST(20001,"用户不存在"),
    USER_CREDENTIAL_ERROR(20002,"密码错误"),
    USER_LOGIN_ERROR_OVER(20003,"登录失败次数超限,请五分钟后重新尝试"),

    USER_SEARCH_ERROR(30000,"搜索用户错误"),

    FILE_ERROR(40001,"文件不合法");


    private int  errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errMsg = errorMsg;
        return this;
    }
}
