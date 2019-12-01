package com.cathetine.simpleChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author:xjk
 * @Date 2019/10/31 13:19
 */
@SpringBootApplication
@MapperScan(basePackages = "com.cathetine.simpleChat.mapper")
@ComponentScan(basePackages= {"com.cathetine.simpleChat", "org.n3r.idworker"})
@EnableSwagger2
public class SimpleChatApplication {

    @Bean
    public SpringUtil getSpingUtil() {
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleChatApplication.class, args);
    }
}
