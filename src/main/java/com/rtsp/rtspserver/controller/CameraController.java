package com.rtsp.rtspserver.controller;

import com.rtsp.rtspserver.config.RtspProperties;
import com.rtsp.rtspserver.model.Camera;
import com.rtsp.rtspserver.service.CameraService;
import com.rtsp.rtspserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cameras")
public class CameraController {

    @Autowired
    private RtspProperties rtspProperties;
    @Autowired
    private CameraService cameraService;
    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<Camera> addCamera(@RequestBody Camera camera) {
        return ResponseEntity.ok(cameraService.addCamera(camera));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCamera(@PathVariable String id) {
        if(cameraService.deleteCamera(Integer.parseInt(id)))
        return ResponseEntity.ok().build();
        return null;
    }

    @GetMapping("/{id}/rtsp-url")
    public ResponseEntity<String> getCameraRtspUrl(@PathVariable int id) {
        String rtspUrl = cameraService.getFullRtspUrl(id, rtspProperties);
        return ResponseEntity.ok(rtspUrl);
    }

    @GetMapping
    public ResponseEntity<List<Camera>> getAllCameras() {
        System.out.println("get all cameras");
        System.out.println(roleService.getAllRoles());
        return ResponseEntity.ok(cameraService.getAllCameras()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<Camera> getCamera(@PathVariable int id) {
        Camera camera = cameraService.getCamera(id);
        if (camera != null) {
            return ResponseEntity.ok(camera);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Camera> updateCamera(@PathVariable int id, @RequestBody Camera camera) {
        Camera updatedCamera = cameraService.updateCamera(id, camera);
        if (updatedCamera != null) {
            return ResponseEntity.ok(updatedCamera);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
