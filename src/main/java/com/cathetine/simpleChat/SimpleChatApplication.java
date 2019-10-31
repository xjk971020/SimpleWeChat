package com.cathetine.simpleChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author:xjk
 * @Date 2019/10/31 13:19
 */
@SpringBootApplication
@MapperScan(basePackages = "com.cathetine.simpleChat.mapper")
@ComponentScan(basePackages= {"com.cathetine.simpleChat", "org.n3r.idworker"})
public class SimpleChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleChatApplication.class, args);
    }
}
