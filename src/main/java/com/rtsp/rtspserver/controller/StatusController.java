package com.rtsp.rtspserver.controller;

import com.rtsp.rtspserver.model.ServerStatus;
import com.rtsp.rtspserver.utils.CameraUtils;
import com.rtsp.rtspserver.utils.ErrorLogger;
import com.rtsp.rtspserver.utils.SystemInfo;
import com.rtsp.rtspserver.utils.UptimeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    @Autowired
    private CameraUtils cameraUtils;
    @GetMapping("/api/status")
    public ServerStatus getServerStatus() {
        long freeDiskSpace = SystemInfo.getFreeDiskSpace();
        int connectedCameras = cameraUtils.getTotalCameras();
        int totalCameras = cameraUtils.getTotalCameras();
        long uptimeMinutes = UptimeManager.getSystemUptime();
        int errorCount = ErrorLogger.getErrorCount();
        System.out.println("free " + freeDiskSpace);

        return new ServerStatus(freeDiskSpace, connectedCameras, totalCameras, uptimeMinutes, errorCount);
    }
}
