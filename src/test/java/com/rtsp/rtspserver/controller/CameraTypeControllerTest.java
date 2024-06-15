package com.rtsp.rtspserver.controller;


import com.rtsp.rtspserver.model.CameraType;
import com.rtsp.rtspserver.service.CameraTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CameraTypeControllerTest {

    @Mock
    private CameraTypeService cameraTypeService;

    @InjectMocks
    private CameraTypeController controller;

    @Test
    void getAllCameraTypes_shouldReturnCameraTypes() {
        CameraType cameraType = new CameraType(1, "Type1", "Description");
        when(cameraTypeService.getAllCameraTypes()).thenReturn(List.of(cameraType));

        ResponseEntity<List<CameraType>> response = controller.getAllCameraTypes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(cameraType);
    }

    @Test
    void getCameraTypeById_shouldReturnCameraType() {
        CameraType cameraType = new CameraType(1, "Type1", "Description");
        when(cameraTypeService.getCameraTypeById(anyInt())).thenReturn(cameraType);

        ResponseEntity<CameraType> response = controller.getCameraTypeById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(cameraType);
    }

    @Test
    void addCameraType_shouldAddCameraType() {
        CameraType cameraType = new CameraType(1, "Type1", "Description");
        doNothing().when(cameraTypeService).addCameraType(any(CameraType.class));

        ResponseEntity<Void> response = controller.addCameraType(cameraType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cameraTypeService, times(1)).addCameraType(cameraType);
    }

    @Test
    void updateCameraType_shouldUpdateCameraType() {
        CameraType cameraType = new CameraType(1, "Type1", "Description");
        when(cameraTypeService.updateCameraType(any(CameraType.class))).thenReturn(true);

        ResponseEntity<Void> response = controller.updateCameraType(1, cameraType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cameraTypeService, times(1)).updateCameraType(cameraType);
    }

    @Test
    void deleteCameraType_shouldDeleteCameraType() {
        when(cameraTypeService.deleteCameraType(anyInt())).thenReturn(true);

        ResponseEntity<Void> response = controller.deleteCameraType(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cameraTypeService, times(1)).deleteCameraType(1);
    }
}

