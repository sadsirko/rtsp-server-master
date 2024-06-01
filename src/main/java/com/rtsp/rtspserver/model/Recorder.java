package com.rtsp.rtspserver.model;

import java.sql.Timestamp;

public class Recorder {
    private int recordId;
    private int cameraId;
    private Timestamp startTime;
    private Timestamp endTime;
    private int durationTime;

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public Recorder(int recordId, int cameraId, Timestamp startTime, Timestamp endTime, int duration ) {
        this.recordId = recordId;
        this.cameraId = cameraId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationTime =duration;
    }

    public Recorder(int cameraId, Timestamp startTime, Timestamp endTime, int duration) {
        this.cameraId = cameraId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationTime =duration;

    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public java.sql.Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public java.sql.Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
