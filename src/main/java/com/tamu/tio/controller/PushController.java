package com.tamu.tio.controller;

import com.tamu.tio.websocket.server.HelloPcaket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tio.core.Tio;
import org.tio.utils.hutool.StrUtil;
import org.tio.websocket.server.WsServerStarter;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

@RestController
@RequestMapping("/push")
public class PushController {

    @Autowired
    private TioWebSocketServerBootstrap bootstrap;
    @GetMapping("/msg")
    public void pushMessage(String msg){
        if (StrUtil.isEmpty(msg)){
            msg = "hello tio websocket spring boot starter";
        }
       // Tio.sendToAll(, new HelloPcaket());
        //Tio.sendToAll(bootstrap., WsResponse.fromText(msg,"utf-8"));
    }
}
