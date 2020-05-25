package com.tamu.tio;

import com.tamu.tio.websocket.server.ShowcaseWebsocketStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tio.websocket.starter.EnableTioWebSocketServer;


@SpringBootApplication
@EnableTioWebSocketServer
public class TioApplication {


    public static void main(String[] args) throws Exception {
        ShowcaseWebsocketStarter.main(args);
        SpringApplication.run(TioApplication.class, args);
    }

}
