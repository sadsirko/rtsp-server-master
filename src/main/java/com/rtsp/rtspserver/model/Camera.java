package com.rtsp.rtspserver.model;

public class Camera {
    private int cameraId;
    private String cameraName;
    private String cameraUrl;
    private int cameraTypeId;

    public Camera() {

    }

    public Camera(int cameraId, String cameraName, String cameraUrl, int cameraTypeId) {
        this.cameraId = cameraId;
        this.cameraName = cameraName;
        this.cameraUrl = cameraUrl;
        this.cameraTypeId = cameraTypeId;
    }
    public Camera( String cameraName, String cameraUrl, int cameraTypeId) {
        this.cameraName = cameraName;
        this.cameraUrl = cameraUrl;
        this.cameraTypeId = cameraTypeId;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    public int getCameraTypeId() {
        return cameraTypeId;
    }

    public void setCameraTypeId(int cameraTypeId) {
        this.cameraTypeId = cameraTypeId;
    }
}
