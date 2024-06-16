package com.rtsp.rtspserver.listeners;

import com.rtsp.rtspserver.model.Camera;
import com.rtsp.rtspserver.server.events.CameraAddedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import jakarta.persistence.PostPersist;

@Component
public class CameraEntityListener {
    private static ApplicationEventPublisher eventPublisher;

    @Autowired
    public void init(ApplicationEventPublisher publisher) {
        CameraEntityListener.eventPublisher = publisher;
    }

    @PostPersist
    public void postPersist(Camera camera) {
        eventPublisher.publishEvent(new CameraAddedEvent(this, camera.getCameraUrl()));
    }
}
