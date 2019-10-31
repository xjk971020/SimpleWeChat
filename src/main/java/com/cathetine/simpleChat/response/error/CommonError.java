package com.cathetine.simpleChat.response.error;

/**
 * @Author:xjk
 * @Date 2019/10/31 15:02
 */
public interface CommonError {
    /**
     * 获取返回状态码
     * @return
     */
    int getErrCode();

    /**
     * 获取返回的信息
     * @return
     */
    String getErrMsg();

    /**
     * 设置错误信息
     * @param errorMsg
     * @return
     */
    CommonError setErrorMsg(String errorMsg);
}
