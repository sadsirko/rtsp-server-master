package com.rtsp.rtspserver;

import com.rtsp.rtspserver.server.RtspServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication()
public class RtspServerApplication {

    public static void main(String[] args) {
//        SpringApplication.run(RtspServerApplication.class, args);
        ApplicationContext ctx = SpringApplication.run(RtspServerApplication.class, args);
        // Запуск RTSP сервера може бути інтегрований тут або як окремий сервіс
        RtspServer rtspServer = ctx.getBean(RtspServer.class);
        rtspServer.startServer();
    }
}
