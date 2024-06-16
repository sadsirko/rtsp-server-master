package com.rtsp.rtspserver.controller;

import com.rtsp.rtspserver.model.RecordRequest;
import com.rtsp.rtspserver.model.Recorder;
import com.rtsp.rtspserver.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;
@RestController
@RequestMapping("/api/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/start")
    public ResponseEntity<?> startRecording(@RequestBody RecordRequest request) {
        System.out.println("request.getDurationTime()");
        System.out.println(request.getDurationTime());
        Recorder record = recordService.startRecording(request.getCameraId(), request.getDurationTime());
        return ResponseEntity.ok(record);
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopRecording(@RequestBody RecordRequest request) {
        boolean success = recordService.stopRecording(request.getCameraId());
        if (success) {
            return ResponseEntity.ok().build();  // Перевірте, чи ця лінія коду досягається під час виконання
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/active/{cameraId}")
    public ResponseEntity<?> checkActiveRecording(@PathVariable int cameraId) {
        Recorder activeRecord = recordService.findActiveRecordByCameraId(cameraId);
        if (activeRecord != null) {
            return ResponseEntity.ok(new AbstractMap.SimpleEntry<>("active", true));
        } else {
            return ResponseEntity.ok(new AbstractMap.SimpleEntry<>("active", false));
        }
    }

}