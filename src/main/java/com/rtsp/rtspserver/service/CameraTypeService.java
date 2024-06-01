package com.rtsp.rtspserver.service;


import com.rtsp.rtspserver.model.CameraType;
import com.rtsp.rtspserver.repository.CameraTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraTypeService {

    @Autowired
    private CameraTypeRepository cameraTypeRepository;

    public List<CameraType> getAllCameraTypes() {
        return cameraTypeRepository.getAllCameraTypes();
    }

    public CameraType getCameraTypeById(int typeId) {
        return cameraTypeRepository.getCameraTypeById(typeId);
    }

    public void addCameraType(CameraType cameraType) {
        cameraTypeRepository.addCameraType(cameraType);
    }

    public boolean updateCameraType(CameraType cameraType) {
        return cameraTypeRepository.updateCameraType(cameraType);
    }

    public boolean deleteCameraType(int typeId) {
        return cameraTypeRepository.deleteCameraType(typeId);
    }
}
