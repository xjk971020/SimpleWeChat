package com.cathetine.simpleChat;

import com.cathetine.simpleChat.netty.WSServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author:xjk
 * @Date 2019/11/5 20:44
 * Netty启动器
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(NettyBooter.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                WSServer.getInstance().start();
                logger.info("Netty service start success");
            } catch (Exception e) {
                logger.error("Netty service start failure");
                e.printStackTrace();
            }
        }
    }
}
