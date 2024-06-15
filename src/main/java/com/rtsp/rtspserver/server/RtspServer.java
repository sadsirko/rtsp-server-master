package com.rtsp.rtspserver.server;

import com.rtsp.rtspserver.model.Camera;
import com.rtsp.rtspserver.repository.CameraRepository;
import com.rtsp.rtspserver.service.CameraService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.rtsp.RtspDecoder;
import io.netty.handler.codec.rtsp.RtspEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Component
public class RtspServer {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    private String serverHost;

    private final int serverPort = 554;

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ChannelFuture channelFuture;

    @Autowired
    private RtspStreamManager rtspStreamManager;
    private RecordingManager recordingManager;
    @Autowired
    private ApplicationContext applicationContext; //
    @Autowired
    public RtspServer() {
//        this.serverPort = serverPort;
//        this.rtspStreamManager = new RtspStreamManager();
//        this.rtspStreamManager = RtspStreamManager.getInstance();

    }

    public String startServer() {
        log.info("RtspServer start");
        try {
            initializeServer();

//            serverHost = "192.168.1.5";
            serverBootstrap.option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_SNDBUF, 128 * 1024)
                    .childOption(ChannelOption.SO_RCVBUF, 64 * 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            RtspServerHandler rtspServerHandler = applicationContext.getBean(RtspServerHandler.class);
                            channel.pipeline().addLast(
                                    new RtspDecoder(),
                                    new RtspEncoder(),
                                    new HttpObjectAggregator(64 * 1024),
                                    rtspServerHandler
                            );
                        }
                    });

            startSendingRtspFrames();

            channelFuture = serverBootstrap.bind(serverHost, serverPort).syncUninterruptibly();

        } catch (Exception e) {
            log.error("RtspServer start failed", e);
            return "";
        }

        log.info("RtspServer started successfully, port: {}", serverPort);
        log.info("RtspServer start successfully, link: rtsp://{}:{}", serverHost, serverPort);
        return String.format("rtsp://%s:%d", serverHost, serverPort);
    }

    private void startSendingRtspFrames() {
        CameraRepository cameraService = new CameraRepository();
        List<Camera> cameras = cameraService.getAllCameras();
        for (Camera camera : cameras) {
            rtspStreamManager.addStream(camera.getCameraUrl());
        }
        rtspStreamManager.sendAllCameras();
        rtspStreamManager.recordingCameras();
    }

    private void resolveLocalAddress() throws IOException {
        Enumeration<InetAddress> addresses = NetworkInterface.getByName("wlan0").getInetAddresses();
        if (addresses.hasMoreElements()) {
            InetAddress localInetAddress = addresses.nextElement();
            serverHost = localInetAddress.getHostAddress();
        } else {
            throw new RuntimeException("No addresses bound to the interface");
        }
        log.info("get address {}", serverHost);

    }

    private void configureServerBootstrap() {
        this.serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup(4);
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            serverBootstrap.channel(NioServerSocketChannel.class);
        }

        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            serverBootstrap.channel(EpollServerSocketChannel.class);
        }

        serverBootstrap.group(bossGroup, workerGroup);
    }

    private void initializeServer() throws IOException, InterruptedException {
        resolveLocalAddress();
        configureServerBootstrap();
    }


    public void close() {
        log.info("RtspServer start to close");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        channelFuture.channel().closeFuture().syncUninterruptibly();
        log.info("RtspServer close successfully");
    }
}