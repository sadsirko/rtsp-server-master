package com.rtsp.rtspserver.server;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;

@Slf4j
public class SingleFrameSender {
    private List<RtspSession> subscribers = new ArrayList<>();
    private String rtspUrl;
    private FFmpegFrameGrabber grabber;
    private FFmpegFrameRecorder recorder;
    private int fileTimeBase;
    private long frameInterval; // Interval between frames in milliseconds
    private double streamFrameRate;
    private long startRealTimeMillis;
    private long nextTimeStamp;
    private long startTimeStamp;
    private boolean isStart;
    private long RECORD_LENGTH = 500;

    private boolean initialized = false;

    long t1;
    long pts = 0;  // Initialize PTS

    private ExecutorService recordingExecutor = Executors.newSingleThreadExecutor();
    public SingleFrameSender(String rtspUrl) {
        this.rtspUrl = rtspUrl;
    }

    public void subscribe(RtspSession rtspSession) {
        log.info("remote ip [{}] subscribe stream", rtspSession.getChannel().remoteAddress().toString());
        subscribers.add(rtspSession);
    }

    public FFmpegFrameGrabber getGrabber()
    {
        return grabber;
    }

    public boolean init() {
        if (!isInitialized()){
        try {
            grabber = new FFmpegFrameGrabber(rtspUrl);
//            FFmpegLogCallback.set();
            log.info("INIT");
//            grabber.setOption("loglevel", "debug");
            grabber.setFormat("rtsp");
            grabber.setOption("allowed_media_types", "video");
            grabber.setOption("rtsp_transport", "tcp");
            grabber.setOption("ignore_err", "1");
            grabber.setOption("stimeout", "300000");
//            grabber.setOption(  "min_port", "32200" );
//            grabber.setOption(  "max_port", "32201" );
            grabber.setOption("buffer_size", "2048000");
            grabber.setOption("reorder_queue_size", "1024");
            log.info("start grab");
            grabber.start();
            setInitialized();
            streamFrameRate = grabber.getFrameRate();
            frameInterval = (long) (1000/streamFrameRate);

            log.info("Grabber started successfully");
            return true;
        } catch (FFmpegFrameGrabber.Exception e) {
            log.error("Failed to initialize RTSP grabber", e);
            return false;
        }}
        return false;
    }

    public void streamFromSource() throws FFmpegFrameGrabber.Exception, FFmpegFrameRecorder.Exception {
        if (!isStart) {
            startRealTimeMillis = System.currentTimeMillis();
            nextTimeStamp = startRealTimeMillis; // Initialize nextTimeStamp
            startTimeStamp = nextTimeStamp; // Keep track of the initial start time
            isStart = true;
        }
        long currentTime = System.currentTimeMillis();
        AVPacket packet;
        while ((packet = grabber.grabPacket()) != null) {
            if (currentTime >= nextTimeStamp) {

                adjustPacketTimestamp(packet, nextTimeStamp - startRealTimeMillis);
//                log.info("Packet PTS: {}, DTS: {}", packet.pts(), packet.dts());
                dispatchPacketToSubscribers(packet);
                nextTimeStamp += frameInterval; // Schedule next frame
            }

            currentTime = System.currentTimeMillis(); // Update current time
            if ((currentTime - startTimeStamp) > RECORD_LENGTH) {
//                log.info("Recording time limit reached.");
                break; // Stop recording after the specified length
            }
        }
    }

    private void adjustPacketTimestamp(AVPacket packet, long expectedTimestamp) {
        if (packet.pts() != avutil.AV_NOPTS_VALUE && packet.dts() != avutil.AV_NOPTS_VALUE) {
            if (packet.dts() > packet.pts()) {
                packet.pts(packet.dts());
            }
        } else {
            packet.pts(expectedTimestamp);
            packet.dts(expectedTimestamp);
        }
    }

    private void dispatchPacketToSubscribers(AVPacket packet) throws FFmpegFrameRecorder.Exception {
        for (RtspSession subscriber : subscribers) {
            AVPacket packetCopy = new AVPacket();
            if (avcodec.av_packet_ref(packetCopy, packet) == 0) {
                try {
                    adjustPacketTimestamp(packetCopy, packet.pts());
                    subscriber.sendPacket(grabber, packetCopy);
//                    log.info("Packet PTS: {}, DTS: {}", packet.pts(), packet.dts());
                } finally {
                    avcodec.av_packet_unref(packetCopy);
                }
            } else {
                log.error("Failed to reference packet for subscriber.");
            }
        }
    }

        public void close() {
        try {
            System.out.println("removing connection inside of SingleSender");
            subscribers.forEach(RtspSession::end);
            subscribers.clear();
            grabber.close();
//            rtspServer.close();
        } catch (FrameGrabber.Exception e) {
            log.error("Close frame grabber failed{}", e.getMessage());
        }
    }

    public void unsubscribe(RtspSession rtspSession) {
        subscribers.remove(rtspSession);
    }

    public long getFrameSendDelay() {
        long nowTime = System.currentTimeMillis();
        return (nextTimeStamp - startTimeStamp) - (nowTime - startRealTimeMillis);
    }

    public String getRtspUrl() {
        return rtspUrl;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized() {
        this.initialized = true;
    }
}
