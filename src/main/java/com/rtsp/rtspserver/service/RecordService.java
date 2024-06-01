package com.rtsp.rtspserver.service;

import com.rtsp.rtspserver.model.Recorder;
import com.rtsp.rtspserver.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public Recorder startRecording(int cameraId, int durationTime) {
        System.out.println("Start recording");
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Recorder record = new Recorder(cameraId, startTime, null, durationTime);
        return recordRepository.addRecord(record);
    }

    public boolean stopRecording(int cameraId) {
        Recorder record = recordRepository.findRecordByCameraId(cameraId);
        if (record != null) {
            record.setEndTime(new Timestamp(System.currentTimeMillis()));
            recordRepository.updateRecord(record);
            return true;
        }
        return false;
    }
    public Recorder findActiveRecordByCameraId(int cameraId) {
        return recordRepository.findRecordByCameraId(cameraId);
    }

}