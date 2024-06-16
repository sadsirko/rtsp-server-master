package com.rtsp.rtspserver.utils;

import com.rtsp.rtspserver.repository.CameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CameraUtils {
    @Autowired
    private CameraRepository cameraRepository;

    public int getTotalCameras() {
        return cameraRepository.getAllCameras().size();
    }
}
