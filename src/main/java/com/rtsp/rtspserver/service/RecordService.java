package com.rtsp.rtspserver.service;

import com.rtsp.rtspserver.model.Recorder;
import com.rtsp.rtspserver.repository.RecordRepository;
import com.rtsp.rtspserver.server.RecordingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordingManager recordingManager;

    @Autowired
    private CameraService cameraService;
    private String savePath = "C:/temp/AH";

    public Recorder startRecording(int cameraId, int duration) {
        String streamId = String.valueOf(cameraId); // Assume stream ID is based on camera ID
        String rtspUrl =  cameraService.getCamera(cameraId).getCameraUrl();
        String outputPath = determineOutputPath(cameraService.getCamera(cameraId).getCameraName());
        long length = duration * 60000L; // Convert minutes to milliseconds
        System.out.println("length");
        System.out.println(length);
        recordRepository.addRecord(new Recorder(cameraId, new Timestamp(System.currentTimeMillis()),null, duration));
        recordingManager.startRecording(streamId, rtspUrl, outputPath, length);
        return new Recorder(cameraId, new Timestamp(System.currentTimeMillis()), null, duration);
    }

    public boolean stopRecording(int cameraId) {
        Recorder record = recordRepository.findRecordByCameraId(cameraId);
        if (record != null) {
            record.setEndTime(new Timestamp(System.currentTimeMillis()));
            recordRepository.updateRecord(record);
            String streamId = String.valueOf(cameraId);
            recordingManager.stopRecording(streamId);
            return true;
        }
        return false;
    }
    private String getRtspUrlForCamera(int cameraId) {
        // Implementation needed
        return "rtsp://example.com/camera/" + cameraId;
    }

    private String determineOutputPath(String cameraName) {
        // Implementation needed
        return savePath + cameraName;
    }
    public Recorder findActiveRecordByCameraId(int cameraId) {
        return recordRepository.findRecordByCameraId(cameraId);
    }

}