package com.rtsp.rtspserver.server.method;

import com.rtsp.rtspserver.server.RtspServerHandler;
import com.rtsp.rtspserver.server.RtspSession;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.rtsp.RtspHeaderNames;
import io.netty.handler.codec.rtsp.RtspVersions;

public class PlayAction implements MethodAction {
    private static final PlayAction INSTANCE = new PlayAction();

    private PlayAction() {
    }

    public static PlayAction getInstance() {
        return INSTANCE;
    }

    @Override
    public FullHttpResponse buildHttpResponse(HttpRequest request, RtspServerHandler serverHandler, RtspSession rtspSession) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, HttpResponseStatus.OK);
        response.headers().set(RtspHeaderNames.CSEQ, request.headers().get(RtspHeaderNames.CSEQ));
        response.headers().set(RtspHeaderNames.SESSION, request.headers().get(RtspHeaderNames.SESSION));
        response.headers().set(RtspHeaderNames.RANGE, request.headers().get(RtspHeaderNames.RANGE));
        rtspSession.subscribeStream();
        return response;
    }
}
