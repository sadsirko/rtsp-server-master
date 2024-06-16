package com.rtsp.rtspserver.server.method;
import com.rtsp.rtspserver.server.RtspServerHandler;
import com.rtsp.rtspserver.server.RtspSession;
import com.rtsp.rtspserver.service.CameraService;
import com.rtsp.rtspserver.service.CameraTypeService;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.rtsp.RtspHeaderNames;
import io.netty.handler.codec.rtsp.RtspVersions;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.PointerPointer;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

import static org.bytedeco.ffmpeg.global.avformat.*;

@Slf4j
public class DescribeAction implements MethodAction {


    @Autowired
    private CameraService cameraService;

    @Autowired
    private CameraTypeService cameraTypeService;
    private static final DescribeAction INSTANCE = new DescribeAction();

    private DescribeAction() {}
//    private String rtspSourceUrl = "rtsp://192.168.168.152:554";
    private final String rtspSourceUrl = "rtsp://192.168.0.138:554";
//    private String rtspSourceUrl = "rtsp://192.168.1.5:554";

    public String getRtspSourceUrl() {
        return rtspSourceUrl;
    }
    public static DescribeAction getInstance() {
        return INSTANCE;
    }
    @Override
    public FullHttpResponse buildHttpResponse(HttpRequest request, RtspServerHandler serverHandler, RtspSession rtspSession) {

//        String sdpDescription = getSdpMsg(rtspSourceUrl);
        log.info("Build describe");
        String sdpDescription = createSdpDescription2(rtspSourceUrl);
//        String sdpDescription = createSdpDescriptionTpLink2(rtspSourceUrl);

//        String sdpDescription = createSdpDescription(rtspSourceUrl);
        FullHttpResponse response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(sdpDescription.getBytes(StandardCharsets.UTF_8)));
        response.headers().set(RtspHeaderNames.CSEQ, request.headers().get(RtspHeaderNames.CSEQ));
        response.headers().set(RtspHeaderNames.CONTENT_TYPE, "application/sdp");
        response.headers().set(RtspHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        log.info("SDP Description sent: {}", sdpDescription);
        return response;
    }

    private String createSdpDescription(String rtspUrl) {
        return "v=0\r\n"
                + "o=- 0 0 IN IP4 127.0.0.1\r\n"
                + "s=No Name\r\n"
                + "c=IN IP4 0.0.0.0 \r\n"
                + "t=0 0\r\n"
                + "m=video 0 RTP/AVP 96\r\n"
                + "a=tool:libavformat 60.16.100\r\n"
                + "a=rtpmap:96 H264/90000\r\n"
                + "a=fmtp:97 packetization-mode=1; sprop-parameter-sets=Z0LgHtoHgUZA,aM4xUg==;\r\n"
                + "b=AS:1035\r\n"
                + "a=control:streamid=0\r\n"
                + "a=fmtp:96 packetization-mode=1; sprop-parameter-sets=Z0LgHtoHgUZA,aM4xUg==; profile-level-id=64001F\r\n";
    }
    private String createSdpDescription2(String rtspUrl) {
        return "v=0\r\n" +
                "o=- 0 0 IN IP4 127.0.0.1\r\n" +
                "s=No Name\r\n" +
                "c=IN IP4 192.168.168.138\r\n" +
                "t=0 0\r\n" +
                "a=tool:libavformat 60.16.100\r\n" +
                "m=video 0 RTP/AVP 96\r\n" +
                "a=rtpmap:96 H264/90000\r\n" +
                "a=fmtp:97 packetization-mode=1; sprop-parameter-sets=Z2QAFqxyBEGx/k5qAgICgAAAAwCAAAAeB4sWwjA=,aOhDhEsiwA==; profile-level-id=640016\r\n" +
                "a=control:streamid=0\r\n"
//                +
//                "m=audio 0 RTP/AVP 97\r\n"
//                + "a=rtpmap:97 MPEG4-GENERIC/44100/2\r\n"
//                + "a=fmtp:97 profile-level-id=1;mode=AAC-hbr;sizelength=13;indexlength=3;indexdeltalength=3; config=1210\r\n" +
//                "a=control:streamid=1"
                ;
    }

    private String createSdpDescriptionTpLink2(String rtspUrl) {
        return "v=0\r\n"
                + "o=- 1293913308916078 1 IN IP4 "+ rtspUrl+"\r\n"
                + "s=RTSP/RTP stream 2 from DCS-2103\r\n"
                + "i=live2.sdp\r\n"
                + "c=IN IP4 0.0.0.0\r\n"
                + "t=0 0\r\n"
                + "a=tool:libavformat 60.16.100\r\n"
                + "a=type:unicast\r\n"
                + "a=control:*\r\n"
                + "a=range:npt=0-\r\n"
                + "a=x-qt-text-nam:RTSP/RTP stream 2 from DCS-2103\r\n"
                + "a=x-qt-text-inf:live2.sdp\r\n"
                + "m=video 0 RTP/AVP 96\r\n"
                + "c=IN IP4 0.0.0.0\r\n"
                + "a=rtpmap:96 H264/90000\r\n"
                + "a=fmtp:96 packetization-mode=1; profile-level-id=640028; sprop-parameter-sets=Z2QAKK2EBUViuKxUdCAqKxXFYqOhAVFYrisVHQgKisVxWKjoQFRWK4rFR0ICorFcVio6ECSFITk8nyfk/k/J8nm5s00IEkKQnJ5Pk/J/J+T5PNzZprQFAX/LgKpAAAADAEAAACWYEAALcbAADN/i974XhEI1,aO48sA==\r\n"
                + "a=control:streamid=0\r\n";
    }


    private String getSdpMsg(String rtspUrl) {
        AVFormatContext avFormatContext = avformat.avformat_alloc_context();

        // Налаштування параметрів для відкриття потоку
        AVDictionary options = new AVDictionary(null);
        avutil.av_dict_set(options, "rtsp_transport", "tcp", 0); // Використання TCP для RTSP
        avutil.av_dict_set(options, "stimeout", "5000000", 0); // 5-секундний тайм-аут для читання

        int res = avformat.avformat_open_input(avFormatContext, rtspUrl, null, options);
        avutil.av_dict_free(options); // Очистка пам'яті після використання

        if (res < 0) {
            byte[] errBuf = new byte[1024];
            avutil.av_strerror(res, errBuf, errBuf.length);
            String errMsg = new String(errBuf, StandardCharsets.UTF_8);
            log.error("Could not open input stream {}, error code: {}, description: {}", rtspUrl, res, errMsg);
            avformat.avformat_free_context(avFormatContext);
            return "";
        }

        if (avformat.avformat_find_stream_info(avFormatContext, (PointerPointer<?>) null) < 0) {
            log.error("Could not find stream information for {}", rtspUrl);
            avformat.avformat_free_context(avFormatContext);
            return "";
        }

        byte[] sdpData = new byte[2048];
        if (av_sdp_create(avFormatContext, 1, sdpData, sdpData.length) < 0) {
            log.error("Could not create SDP for {}", rtspUrl);
            avformat.avformat_free_context(avFormatContext);
            return "";
        }

        String sdpMsg = new String(sdpData, StandardCharsets.UTF_8).trim();
        avformat.avformat_free_context(avFormatContext);
        log.info(sdpMsg);
        return sdpMsg;
    }

}
