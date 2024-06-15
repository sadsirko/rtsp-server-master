package com.rtsp.rtspserver.server.events;
import org.springframework.context.ApplicationEvent;
public class CameraAddedEvent extends ApplicationEvent{
    private final String cameraUrl;

    public CameraAddedEvent(Object source, String cameraUrl) {
        super(source);
        this.cameraUrl = cameraUrl;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }
}
