package com.rtsp.rtspserver.model;

import java.sql.Timestamp;

public class ConnectionLog {
    private int connectionId;
    private int cameraId;
    private Timestamp connectionTime;
    private Timestamp disconnectionTime;

    public ConnectionLog(int cameraId, Timestamp connectionTime, Timestamp disconnectionTime) {
        this.cameraId = cameraId;
        this.connectionTime = connectionTime;
        this.disconnectionTime = disconnectionTime;
    }

    public ConnectionLog(int connectionId, int cameraId, java.sql.Timestamp connectionTime, java.sql.Timestamp disconnectionTime) {

    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public java.sql.Timestamp getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(Timestamp connectionTime) {
        this.connectionTime = connectionTime;
    }

    public java.sql.Timestamp getDisconnectionTime() {
        return disconnectionTime;
    }

    public void setDisconnectionTime(Timestamp disconnectionTime) {
        this.disconnectionTime = disconnectionTime;
    }
// Getters and setters
}