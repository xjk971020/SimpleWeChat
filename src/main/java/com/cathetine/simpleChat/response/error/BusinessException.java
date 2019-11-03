package com.cathetine.simpleChat.response.error;

/**
 * @Author:xjk
 * @Date 2019/10/31 15:03
 */
public class BusinessException extends RuntimeException implements CommonError {

    private CommonError commonError;

    /**
     * 接受EmBusinessError的参数
     *
     * @param commonError
     */
    public BusinessException(CommonError commonError) {
        this.commonError = commonError;
    }

    /**
     * 传入自定义错误信息
     *
     * @param commonError
     * @param errMsg
     */
    public BusinessException(CommonError commonError, String errMsg) {
        this.commonError = commonError;
        this.commonError.setErrorMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.commonError.setErrorMsg(errorMsg);
        return this;
    }
}
