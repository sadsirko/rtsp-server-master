package com.rtsp.rtspserver.service;


import com.rtsp.rtspserver.config.RtspProperties;
import com.rtsp.rtspserver.model.Camera;
import com.rtsp.rtspserver.repository.CameraRepository;
import com.rtsp.rtspserver.server.RtspStreamManager;
import com.rtsp.rtspserver.server.events.CameraAddedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraService {

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RtspStreamManager rtspStreamManager;

    public Camera addCamera(Camera camera) {
        Camera newCamera = cameraRepository.addCamera(camera);
        if (newCamera != null) {
            eventPublisher.publishEvent(new CameraAddedEvent(this, newCamera.getCameraUrl()));
        }
        return newCamera;
    }

    public boolean deleteCamera(int cameraId) {
        Camera camera = cameraRepository.findById(cameraId);
        boolean removed = cameraRepository.delete(cameraId);
        if (removed) {
            System.out.println("removing");
            rtspStreamManager.removeStream(camera.getCameraUrl());
        }
        return removed;
    }
    public Camera getCamera(int id) {
        return cameraRepository.findById( id);
    }

    public String getFullRtspUrl(int cameraId, RtspProperties rtspProperties) {
        Camera camera = cameraRepository.findById(cameraId);
        return rtspProperties.getHost() + ":" + rtspProperties.getPort()+ "/" + camera.getCameraUrl();
    }
    public List<Camera> getAllCameras() {
        return cameraRepository.getAllCameras();
    }
    public Camera updateCamera(int cameraId, Camera camera) {
        Camera existingCamera = cameraRepository.findById(cameraId);
        if (existingCamera != null) {
            existingCamera.setCameraName(camera.getCameraName());
            existingCamera.setCameraUrl(camera.getCameraUrl());
            existingCamera.setCameraTypeId(camera.getCameraTypeId());
            if (cameraRepository.updateCamera(existingCamera)) {
                return existingCamera;
            }
        }
        return null;
    }

}
