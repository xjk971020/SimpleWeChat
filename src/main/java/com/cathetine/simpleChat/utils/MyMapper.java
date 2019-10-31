package com.cathetine.simpleChat.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Author:xjk
 * @Date 2019/10/31 12:48
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
