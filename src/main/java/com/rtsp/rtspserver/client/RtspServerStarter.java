package com.rtsp.rtspserver.client;

import com.rtsp.rtspserver.server.RtspServer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class RtspServerStarter {
    public static void main(String[] args) {
//        RtspServer rtspServer = new RtspServer(new File("E:/saved_videos/test2.mp4"),554 );
        RtspServer rtspServer = new RtspServer();
//        RtspServer rtspServer = new RtspServer(554, "rtsp://admin:1234qwert@192.168.1.64:554/streaming/channels/101");
        rtspServer.startServer();
    }
}
