package com.rtsp.rtspserver.model;


public class ServerStatus {
    private long freeDiskSpace;
    private int connectedCameras;
    private int activeCameras;
    private long uptimeMinutes;
    private int errorCount;

    // Конструктори, геттери та сеттери
    public ServerStatus(long freeDiskSpace, int connectedCameras, int activeCameras, long uptimeMinutes, int errorCount) {
        this.freeDiskSpace = freeDiskSpace;
        this.connectedCameras = connectedCameras;
        this.activeCameras = activeCameras;
        this.uptimeMinutes = uptimeMinutes;
        this.errorCount = errorCount;
    }

    public long getFreeDiskSpace() {
        return freeDiskSpace;
    }

    public void setFreeDiskSpace(long freeDiskSpace) {
        this.freeDiskSpace = freeDiskSpace;
    }

    public int getConnectedCameras() {
        return connectedCameras;
    }

    public void setConnectedCameras(int connectedCameras) {
        this.connectedCameras = connectedCameras;
    }

    public int getActiveCameras() {
        return activeCameras;
    }

    public void setActiveCameras(int activeCameras) {
        this.activeCameras = activeCameras;
    }

    public long getUptimeMinutes() {
        return uptimeMinutes;
    }

    public void setUptimeMinutes(int uptimeMinutes) {
        this.uptimeMinutes = uptimeMinutes;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
}