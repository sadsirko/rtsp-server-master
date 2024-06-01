
package com.rtsp.rtspserver.server;

import com.rtsp.rtspserver.server.events.CameraAddedEvent;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
public class RtspStreamManager {

    private ConcurrentHashMap<String, SingleFrameSender> frameSenders;
    private AtomicInteger idGenerator;
    private ScheduledExecutorService executor ;
    //    private ExecutorService recordingExecutor = Executors.newCachedThreadPool();
    public RtspStreamManager() {
        frameSenders = new ConcurrentHashMap<>();
        idGenerator = new AtomicInteger(0);
        executor = Executors.newScheduledThreadPool(1);
//        recordingExecutor = Executors.newCachedThreadPool();
    }

    // Метод для додавання нового потоку
    public void addStream(String streamUrl) {
        SingleFrameSender sender = new SingleFrameSender(streamUrl);
        sender.init();
        frameSenders.put(streamUrl, sender);
//        log.info("added stream {}",  frameSenders.get(streamUrl).toString());
        log.info("added stream {}",  frameSenders.get(streamUrl).getRtspUrl());
        printAllStreams();
//        System.out.println(frameSenders.get(streamUrl).toString());
    }
    public void printAllStreams() {
        log.info("all_streams");

        frameSenders.forEach((id, sender) -> {
            System.out.println( sender.getRtspUrl());
        });
    }
    // Метод для видалення потоку
    public boolean removeStream(String streamUrl) {
        printAllStreams();
        SingleFrameSender sender = frameSenders.remove(streamUrl);
        log.info("removing connection inside of Manager");

        if (sender != null) {
            System.out.println("removing connection inside because sender");
            sender.close();  // Закрити і звільнити ресурси
            return true;
        }
        printAllStreams();

        return false;
    }

    public void printAllRtspUrls() {
        System.out.println("List of RTSP URLs:");
        System.out.println("------------------------------");
        frameSenders.forEach((url, sender) -> {
            System.out.println(url);
        });
        System.out.println("------------------------------");
    }

    // Метод для отримання `RtspFrameSender` за ID
    public SingleFrameSender getSender(String url) {
        return frameSenders.get(url);
    }

    public void sendAllCameras() {
        frameSenders.forEach((id, sender) -> {
//            log.info("lsend all {}",id);
            long delay = sender.getFrameSendDelay();  // Затримка визначається в кожному sender'і
            executor.schedule(() -> {
                try {
//                    log.info("Sending frame{} id {}",delay,id);
                    sender.streamFromSource();  // Відправка кадру
                } catch (FFmpegFrameGrabber.Exception | FFmpegFrameRecorder.Exception e) {
                    throw new RuntimeException(e);
                }
                sendAllCameras();
            }, delay, TimeUnit.MILLISECONDS);
        });
    }
    public void recordingCameras(){
        RecordingManager recordingManager = new RecordingManager();
//        recordingManager.startRecording("fff","rtsp://admin:qwert12345@192.168.1.64/Streaming/channels/101",
//                "C:/temp/AH",40000);
//        recordingManager.startRecording("fffa","rtsp://rtsp-test-server.viomic.com:554/stream",
//                "C:/temp/vio",40000);
//        recordingManager.startRecording("ffa","rtsp://192.168.0.117:554/live2.sdp",
//                "C:/temp/camera117",40000);
//        recordingManager.startRecording("ffg","rtsp://rtspstream:b1c92cda2507b01c4f02b452c65b56c0@zephyr.rtsp.stream/pattern",
//                "C:/temp/A1",100000);

    }

//    public void streamFromSource() {
//        recordingExecutor.submit(() -> {
//            try {
//                init();  // Ініціалізація grabber та recorder
//                long currentTime = System.currentTimeMillis();
//                AVPacket packet;
//                while ((packet = grabber.grabPacket()) != null && (System.currentTimeMillis() - currentTime < RECORD_LENGTH_2)) {
//                    recorder.recordPacket(packet);  // Асинхронний запис пакета
//                }
//                close();  // Закриття записувача та захоплювача
//            } catch (FFmpegFrameGrabber.Exception | FFmpegFrameRecorder.Exception e) {
//                log.error("Error during streaming from source: " + e.getMessage());
//            }
//        });
//    }

    @EventListener
    public void handleCameraAdded(CameraAddedEvent event) {
        log.info("added stream");
        addStream(event.getCameraUrl());
    }

    // Закриття всіх потоків
    public void closeAll() {
        frameSenders.values().forEach(SingleFrameSender::close);
        frameSenders.clear();
    }
}