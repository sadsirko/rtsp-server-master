package com.rtsp.rtspserver.controller;

import com.rtsp.rtspserver.config.RtspProperties;
import com.rtsp.rtspserver.model.Camera;
import com.rtsp.rtspserver.service.CameraService;
import com.rtsp.rtspserver.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CameraControllerTest {

    @Mock
    private CameraService cameraService;

    @Mock
    private RoleService roleService;

    @Mock
    private RtspProperties rtspProperties;

    @InjectMocks
    private CameraController controller;

    private Camera camera;

    @BeforeEach
    void setUp() {
        camera = new Camera(1, "Camera1", "rtsp://192.168.1.5:554", 1);
    }

    @Test
    void addCamera_shouldAddCamera() {
        when(cameraService.addCamera(any(Camera.class))).thenReturn(camera);

        ResponseEntity<Camera> response = controller.addCamera(new Camera("New Camera", "rtsp://192.168.1.5:554", 1));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(camera);
        verify(cameraService, times(1)).addCamera(any(Camera.class));
    }

    @Test
    void deleteCamera_shouldDeleteCamera() {
        when(cameraService.deleteCamera(anyInt())).thenReturn(true);

        ResponseEntity<Void> response = controller.deleteCamera("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cameraService, times(1)).deleteCamera(1);
    }

    @Test
    void getCameraById_shouldReturnCamera() {
        when(cameraService.getCamera(anyInt())).thenReturn(camera);

        ResponseEntity<Camera> response = controller.getCamera(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(camera);
    }

    @Test
    void getAllCameras_shouldReturnCameraList() {
        List<Camera> cameras = new ArrayList<>();
        cameras.add(camera);
        when(cameraService.getAllCameras()).thenReturn(cameras);

        ResponseEntity<List<Camera>> response = controller.getAllCameras();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0)).isEqualTo(camera);
    }

//    @Test
//    void updateCamera_shouldUpdateCamera() {
//        when(cameraService.updateCamera(anyInt(), any(Camera.class))).thenReturn(camera);
//
//        ResponseEntity<Camera> response = controller.updateCamera(1, new Camera("Updated Camera", "rtsp://192.168.1.5:554", 1));
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo(camera);
//        verify(cameraService, times(1)).updateCamera(1, new Camera("Updated Camera", "rtsp://192.168.1.5:554", 1));
//    }

    @Test
    void getCameraRtspUrl_shouldReturnUrl() {
        when(cameraService.getFullRtspUrl(anyInt(), any(RtspProperties.class))).thenReturn("rtsp://192.168.1.5:554/stream");

        ResponseEntity<String> response = controller.getCameraRtspUrl(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("rtsp://192.168.1.5:554/stream");
    }
}
