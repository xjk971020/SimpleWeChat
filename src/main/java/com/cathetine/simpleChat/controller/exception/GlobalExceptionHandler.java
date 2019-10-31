package com.cathetine.simpleChat.controller.exception;

import com.cathetine.simpleChat.response.CommonReturnType;
import com.cathetine.simpleChat.response.error.BussinessException;
import com.cathetine.simpleChat.response.error.EmBusinessError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:xjk
 * @Date 2019/10/31 15:39
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BussinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonReturnType bussinessExceptionHandler(BussinessException ex) {
        logger.error("find bussiness exception: {}",ex.getErrMsg());
        Map<String,Object> responseData = new HashMap<>();
        responseData.put("errorCode",ex.getErrCode());
        responseData.put("errorMessage",ex.getErrMsg());
        return CommonReturnType.create(responseData,"fail");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonReturnType exceptionhandler(Exception ex) {
        ex.printStackTrace();
        logger.error("find bussiness exception: {}",ex.getMessage());
        Map<String,Object> responseData = new HashMap<>();
        responseData.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
        responseData.put("errorMessage",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        return CommonReturnType.create(responseData,"fail");
    }
}