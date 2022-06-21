package com.nju.nnt.config;

import com.nju.nnt.websocket.WSServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class NettybootServerInitConfig  implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){
            //只在root容器被启动后调用，避免被初始化两次
            WSServer.getInstance().start();
        }
    }
}
