package com.rtsp.rtspserver.controller;

import com.rtsp.rtspserver.model.CameraType;
import com.rtsp.rtspserver.service.CameraTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camera-types")
public class CameraTypeController {

    @Autowired
    private CameraTypeService cameraTypeService;

    @GetMapping
    public ResponseEntity<List<CameraType>> getAllCameraTypes() {
        return ResponseEntity.ok(cameraTypeService.getAllCameraTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CameraType> getCameraTypeById(@PathVariable int id) {
        return ResponseEntity.ok(cameraTypeService.getCameraTypeById(id));
    }

    @PostMapping
    public ResponseEntity<Void> addCameraType(@RequestBody CameraType cameraType) {
        cameraTypeService.addCameraType(cameraType);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCameraType(@PathVariable int id, @RequestBody CameraType cameraType) {
        cameraType.setTypeId(id);
        if (cameraTypeService.updateCameraType(cameraType)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCameraType(@PathVariable int id) {
        if (cameraTypeService.deleteCameraType(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
