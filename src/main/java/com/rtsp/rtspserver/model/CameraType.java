package com.rtsp.rtspserver.model;

public class CameraType {
    private int typeId;
    private String typeName;
    private String sdpText;

    public CameraType(int typeId, String typeName, String sdpText) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.sdpText = sdpText;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSdpText() {
        return sdpText;
    }

    public void setSdpText(String sdpText) {
        this.sdpText = sdpText;
    }
}
