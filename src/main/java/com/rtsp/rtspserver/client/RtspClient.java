package com.rtsp.rtspserver.client;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import javax.swing.*;

import static org.bytedeco.ffmpeg.global.avutil.av_log_set_level;

@Slf4j
public class RtspClient {
    public static void main(String[] args) {
//        pullRtspStream("rtsp://rtsp-test-server.viomic.com:554/stream");
        pullRtspStream("rtsp://192.168.168.152:554");
//        pullRtspStream("rtsp://172.21.80.1:554");
//        pullRtspStream("rtsp://192.168.1.5:554");

    }

    public static void pullRtspStream(String rtspAddress) {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(rtspAddress);
        av_log_set_level(avutil.AV_LOG_DEBUG); // Set FFmpeg logging level to DEBUG
        try {
            configureGrabber(grabber);
            grabber.start();
            displayVideo(grabber);
            grabber.close();
        } catch (FrameGrabber.Exception e) {
            log.error("Error during RTSP streaming: {}", e.getMessage(), e);
        }
    }

    private static void configureGrabber(FFmpegFrameGrabber grabber) {
        grabber.setFormat("rtsp");
        grabber.setOption("allowed_media_types", "video");
        grabber.setOption("rtsp_transport", "udp"); // Prefer TCP for stable streaming
        grabber.setOption("analyzeduration", "10000000"); // 10 seconds
        grabber.setOption("probesize", "50000000"); // 50 MB
        grabber.setOption("reorder_queue_size", "1024"); // Queue size for packet reordering
    }

    private static void displayVideo(FFmpegFrameGrabber grabber) throws FrameGrabber.Exception {
        CanvasFrame canvasFrame = new CanvasFrame("Video Capture", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(true);

        Frame frame;
        while ((frame = grabber.grabImage()) != null) {
            canvasFrame.showImage(frame);
            if (!canvasFrame.isDisplayable()) {
                break; // Stop if the display window is closed
            }
        }
        canvasFrame.dispose(); // Dispose of the window once done
    }
}
