package com.rtsp.rtspserver.server;

import com.rtsp.rtspserver.server.method.MethodAction;
import com.rtsp.rtspserver.server.method.MethodFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.rtsp.RtspVersions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Scope("prototype")
public class RtspServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Getter
    private String rtspSourceUrl;

    private final RtspStreamManager rtspStreamManager;

    private final Map<Channel, RtspSession> lstChannels = new HashMap<>();

    @Autowired
    public RtspServerHandler(RtspStreamManager rtspStreamManager) {
        this.rtspStreamManager = rtspStreamManager;
    }

    public String getRtspSourceUrl() {
        return rtspSourceUrl;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (!lstChannels.containsKey(channel)) {
            lstChannels.put(channel, null);
//            log.info("New channel registered: {}", channel);
        } else {
            log.warn("Channel already registered: {}", channel);
        }
    }

    protected static String extractURL(String input) {
        if (input == null) {
            return "Invalid URL";
        }
        int portIndex = input.indexOf(":");
        if (portIndex != -1) {
            int secondSlashIndex = input.indexOf("/", portIndex + 3);
            if (secondSlashIndex != -1) {
                String cutEnd = "/streamid=0";
                System.out.println(input.substring(secondSlashIndex +1, input.length()-cutEnd.length() + 1));
                System.out.println(input.substring(secondSlashIndex +1));
                return input.substring(secondSlashIndex +1);
            }
        }
        return "Invalid URL";
    }
    protected static String extractURLWithoutId(String input) {
        int portIndex;
        if (input == null)
            return "Invalid URL";
        portIndex = input.indexOf(":");
        if (portIndex != -1) {
            int secondSlashIndex = input.indexOf("/", portIndex + 3);
            if (secondSlashIndex != -1) {
                String cutEnd = "/streamid=0";
                String substring = input.substring(secondSlashIndex + 1, input.length() - cutEnd.length() );
//                System.out.println(substring);
//                System.out.println(input.substring(secondSlashIndex +1));
                return substring; // Повертаємо підстроку починаючи з "/" до кінця
            }
        }
        return "Invalid URL";
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        Channel channel = ctx.channel();
        HttpMethod method = request.method();
        String uri = request.uri();
        log.info(uri);
        String streamUrl = extractURL(uri);
        String streamUrlSETUP = extractURLWithoutId(uri);


        SingleFrameSender sender = rtspStreamManager.getSender(streamUrl);
        log.info("streamUrl {}", streamUrl);
        log.info("streamUrl SETUP{}", streamUrlSETUP);
        if (sender == null) {
//            sender = rtspStreamManager.getSender(streamUrl);
            sender = rtspStreamManager.getSender(streamUrlSETUP);
            if (sender == null) {
                rtspStreamManager.printAllRtspUrls();
                log.info("sender{}", sender.toString());
                sendNotFoundResponse(ctx);
                return;
            }
        }

        RtspSession session = lstChannels.get(channel);

        if (session == null) {
            log.info("get it {}", sender.getRtspUrl());
            session = new RtspSession(channel, sender);
            lstChannels.put(channel, session);
        } else {
            log.info("already have channel {}", sender.getRtspUrl());
            session.updateSender(sender);
        }

        MethodAction methodAction = MethodFactory.getMethodAction(method);
        FullHttpResponse response = methodAction.buildHttpResponse(request, this, session);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        lstChannels.remove(ctx.channel());
        ctx.close();
    }
    private void sendNotFoundResponse(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                RtspVersions.RTSP_1_0, HttpResponseStatus.NOT_FOUND,
                Unpooled.copiedBuffer("Stream not found".getBytes(StandardCharsets.UTF_8))
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}