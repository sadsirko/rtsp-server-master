package com.rtsp.rtspserver.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegLogCallback;

import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import static org.bytedeco.ffmpeg.global.avutil.av_log_set_level;

@Slf4j
public class RtspSession {
    private static final AtomicInteger RTP_PORT = new AtomicInteger(34542);

    private FFmpegFrameRecorder recorder;
    private FFmpegFrameRecorder recorderSave;

    private SingleFrameSender frameSender;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd__hhmmSSS");

    private static final boolean AUDIO_ENABLED = false;

    @Getter
    private final Channel channel;

    private String rtpUrl;

    private double timeStamp;

    private double timeStep;

    public RtspSession(Channel channel, SingleFrameSender frameSender) {
        this.channel = channel;
        this.frameSender = frameSender;
        this.channel.closeFuture().addListener((ChannelFutureListener) future -> end());
    }
    public void updateSender(SingleFrameSender newSender) {
        if (this.frameSender != newSender) {
            this.frameSender.unsubscribe(this);
            this.frameSender = newSender;
            this.frameSender.subscribe(this);

            if (recorder != null) {
                try {
                    recorder.stop();
                    recorder.release();
                    prepareFrameRecorder(newSender.getGrabber());
                } catch (FFmpegFrameRecorder.Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RtspSession) {
            return this.channel.compareTo(((RtspSession) obj).channel) == 0;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.channel.hashCode();
    }

    public void subscribeStream() {
        if (frameSender != null) {
            frameSender.subscribe(this);
        }
        channel.closeFuture().addListener((ChannelFutureListener) future -> {
            end();
        });
    }

    public void sendPacket(FFmpegFrameGrabber grabber, AVPacket packet) throws FFmpegFrameRecorder.Exception {
        try {
            if (recorder == null) {
                prepareFrameRecorder(grabber);
            }

            recorder.recordPacket(packet);
        } catch (Exception e) {
            log.error("Error recording packet", e);
        }
    }

    public void end() {
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;
            }
            if (recorderSave != null) {
                recorderSave.stop();
                recorderSave.release();
                recorderSave = null;
            }
            if (channel.isActive()) {
                channel.close().syncUninterruptibly();
                log.info("Channel closed successfully");
            }
        } catch (Exception e) {
            log.error("Failed to close resources properly: {}", e.getMessage());
        } finally {
            frameSender.unsubscribe(this);
        }
    }
    public String parseTransport(String transport) {
        log.info(transport);
        String[] split = transport.split(";");
        String[] remotePort = split[2].split("-");
        int remoteRtpPort = Integer.parseInt(remotePort[0].substring(remotePort[0].indexOf("=") + 1));
        int serverRtpPort = RTP_PORT.getAndIncrement();
        int serverRtcpPort = RTP_PORT.getAndIncrement();
        log.info("remoteport" + remoteRtpPort);
        String remoteIp = getRemoteIp();
        String serverIp;
        try {
            InetAddress localInetAddress = NetworkInterface.getByName("wlan0").getInetAddresses().nextElement();
            serverIp = localInetAddress.getHostAddress();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
//        serverIp = "192.168.1.5";

        rtpUrl = String.format(Locale.ENGLISH, "rtp://%s:%d?localaddr=%s&localrtpport=%d&localrtcpport=%d&connect=1",
                remoteIp, remoteRtpPort, serverIp, serverRtpPort, serverRtcpPort);
        log.info("Generating RTP URL with remote IP: {}, remote RTP port: {}, server IP: {}, server RTP port: {}, server RTCP port: {}",
                remoteIp, remoteRtpPort, serverIp, serverRtpPort, serverRtcpPort);

        log.info(String.format(Locale.ENGLISH, "%s;server_port=%d-%d", transport, serverRtpPort, serverRtcpPort));
        return String.format(Locale.ENGLISH, "%s;server_port=%d-%d", transport, serverRtpPort, serverRtcpPort);
    }

    public String getRemoteIp() {
        SocketAddress remoteAddress = channel.remoteAddress();
        log.info("getIp of user", remoteAddress);
        if (remoteAddress instanceof InetSocketAddress inetSocketAddress) {
            return inetSocketAddress.getAddress().getHostAddress();
        }
        return "unknown";
    }

    private void prepareFrameRecorderSave(FFmpegFrameGrabber grabber) throws FFmpegFrameRecorder.Exception {
        if (recorderSave != null) {
            return; // Recorder is already initialized, do not reinitialize
        }

        int audioChannel = AUDIO_ENABLED ? 1 : 0;
        av_log_set_level(avutil.AV_LOG_INFO);
        String outputFile = "C:\\temp\\" + DATE_FORMAT.format(new Date()) + "_packetRecord.mp4";

        recorderSave = new FFmpegFrameRecorder(outputFile, grabber.getImageWidth(), grabber.getImageHeight(), audioChannel);
        recorderSave.setFrameRate(grabber.getFrameRate());
        recorderSave.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorderSave.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorderSave.setFormat("mp4");
        recorderSave.start(grabber.getFormatContext());
    }

    private void prepareFrameRecorder(FFmpegFrameGrabber grabber) throws FFmpegFrameRecorder.Exception {
        av_log_set_level(avutil.AV_LOG_INFO);
        int width = grabber.getImageWidth();
        int height = grabber.getImageHeight();

        log.info("width, height {} {} {} {}", width, height, grabber.getVideoBitrate(), grabber.getVideoCodecName());
        recorder = new FFmpegFrameRecorder(rtpUrl, width, height);
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // Using H.264 for video codec
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // Ensure the pixel format is compatible
        recorder.setFormat("rtp");

        log.info(String.valueOf(grabber.getFrameRate()));
        AVFormatContext formatContext = grabber.getFormatContext();
        recorder.start(formatContext);
    }
}








