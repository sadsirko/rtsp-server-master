package com.rtsp.rtspserver.model;

import org.springframework.beans.factory.annotation.Autowired;

public class RecordRequest {
    private Integer cameraId;  // ID камери для старту запису
    private Integer recordId;  // ID запису для зупинки запису
    private Integer durationTime; // Тривалість запису в секундах

    // Конструктори, гетери та сетери
    public RecordRequest(int cameraId, int durationTime) {
        this.cameraId =cameraId;
        this.durationTime = durationTime;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }
}
