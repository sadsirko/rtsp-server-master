package com.rtsp.rtspserver.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

import org.bytedeco.ffmpeg.avcodec.AVPacket;

@Slf4j
public class PacketRecorder {
    private FFmpegFrameGrabber grabber;
    private FFmpegFrameRecorder recorder;
    private boolean isRecording;

    private String inputUrl;
    private String outputUrl;
    private long recordLength;

    public PacketRecorder(String inputUrl, String outputUrl, long recordLength) {
        this.inputUrl = inputUrl;
        this.outputUrl = outputUrl;
        this.recordLength = recordLength;
    }
    

    void setupRecorder() throws FrameGrabber.Exception, FrameRecorder.Exception {
        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd__hhmmSSS");
        grabber = new FFmpegFrameGrabber(this.inputUrl);
        grabber.setFormat("rtsp");
        grabber.setOption("allowed_media_types", "video");
//        grabber.setOption("rtsp_transport", "tcp");
        grabber.setOption("stimeout", "300000");
        grabber.setOption("buffer_size", "2048000");
        grabber.setOption("reorder_queue_size", "1024");
        grabber.start();
//        String nameOut = this.output + "_" + DATE_FORMAT.format(new Date()) + "result.mp4";
        recorder = new FFmpegFrameRecorder(this.outputUrl + "_" + DATE_FORMAT.format(new Date()) + "result.mp4", grabber.getImageWidth(), grabber.getImageHeight());
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setFormat("mp4");
        recorder.start(grabber.getFormatContext());

        isRecording = true;
    }
    void reSetupRecorder() throws FrameGrabber.Exception, FrameRecorder.Exception {
        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd__hhmmSSS");
//        String nameOut = this.output + "_" + DATE_FORMAT.format(new Date()) + "result.mp4";
        recorder = new FFmpegFrameRecorder(this.outputUrl + "_" + DATE_FORMAT.format(new Date()) + "result.mp4", grabber.getImageWidth(), grabber.getImageHeight());
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setFormat("mp4");
        recorder.start(grabber.getFormatContext());

        isRecording = true;
    }

    public void startRecording() throws FrameGrabber.Exception, FrameRecorder.Exception {
        log.info("start recording {}", outputUrl);
        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd__hhmmSSS");
        setupRecorder();
        AVPacket packet;
        long t1 = System.currentTimeMillis();
        long pts = 0;  // Initialize PTS
        log.info(String.valueOf(grabber.grabPacket()));
        while ((packet = grabber.grabPacket()) != null) {
//            if (packet.dts() != avutil.AV_NOPTS_VALUE) {
//                pts = packet.dts();
//            } else {
//                packet.pts(pts);
//            }
//            packet.dts(pts);
//            packet.pts(pts);
//            pts += 1;
//            log.info("packet saved");
            recorder.recordPacket(packet); // Record each packet
            if ((System.currentTimeMillis() - t1) > recordLength) {

                System.out.println("end t1 {}");
                try {
                    stopRecording();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                reSetupRecorder();
                startRecording();
                break; // Stop recording after the specified
            }
    }
}

    public void stopRecording() throws Exception {
        log.info("stop recording {}", outputUrl);
        if (recorder != null) {
            recorder.stop();
            recorder.release();
        }

        isRecording = false;
    }
    public void stopGrabbing() throws Exception {
        log.info("stop grabbing {}", outputUrl);
        if (grabber != null) {
            grabber.stop();
            grabber.release();
        }
        isRecording = false;
    }

    public void stopAllRecording() throws Exception {
        log.info("stop All recording {}", outputUrl);
        if (recorder != null) {
            recorder.stop();
            recorder.release();
        }
        if (grabber != null) {
            grabber.stop();
            grabber.release();
        }
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }
}

