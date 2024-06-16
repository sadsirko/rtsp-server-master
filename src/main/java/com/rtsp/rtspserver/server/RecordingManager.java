package com.rtsp.rtspserver.server;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
@Slf4j
@Component
public class RecordingManager {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<String, Future<?>> recordings = new ConcurrentHashMap<>();
    private final Map<String, PacketRecorder> recorders = new ConcurrentHashMap<>();

    public synchronized void startRecording(String streamId, String inputUrl, String outputUrl, long length) {
        log.info("lenght {}",length);
        if (!recordings.containsKey(streamId)) {
            if (recorders.containsKey(streamId)) {
                PacketRecorder existingRecorder = recorders.get(streamId);
                if (existingRecorder.isRecording()) {
                    return; // Якщо вже записується, пропускаємо запуск нового
                }
            }
            PacketRecorder recorder = new PacketRecorder(inputUrl, outputUrl, length);
            Future<?> future = executor.submit(() -> {
                try {
//                    recorder.setupRecorder();
                    recorder.startRecording();
                } catch (Exception e) {
                    log.error("Error during recording: {}", e.getMessage());
                } finally {
                    recordings.remove(streamId);
                    recorders.put(streamId, recorder); // Збереження стану записувача
                }
            });
            recordings.put(streamId, future);
            recorders.put(streamId, recorder);
        }
    }

    public synchronized void stopRecording(String streamId) {

        Future<?> future = recordings.get(streamId);
        PacketRecorder recorder = recorders.get(streamId);
        if (future != null && recorder != null) {
            try {
                recorder.stopAllRecording(); // Зупинка запису
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            future.cancel(true);
            recordings.remove(streamId);
            recorders.remove(streamId);
        }
    }

    public boolean isRecording(String streamId) {
        return recordings.containsKey(streamId) && recorders.get(streamId).isRecording();
    }
}
