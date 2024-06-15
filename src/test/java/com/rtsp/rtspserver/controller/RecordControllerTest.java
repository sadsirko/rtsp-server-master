package com.rtsp.rtspserver.controller;

import com.rtsp.rtspserver.model.RecordRequest;
import com.rtsp.rtspserver.model.Recorder;
import com.rtsp.rtspserver.service.RecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordControllerTest {

    @Mock
    private RecordService recordService;

    @InjectMocks
    private RecordController controller;

    @Test
    void startRecording_shouldStartRecording() {
        RecordRequest request = new RecordRequest(1, 60);
        Recorder recorder = new Recorder(1, 1, null, null, 60);
        when(recordService.startRecording(anyInt(), anyInt())).thenReturn(recorder);

        ResponseEntity<?> response = controller.startRecording(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(recorder);
    }

//    @Test
//    void stopRecording_shouldStopRecording() {
//        RecordRequest request = new RecordRequest(1, 60);  // Припускаю, що durationTime = 60 для прикладу
//        when(recordService.stopRecording(anyInt())).thenReturn(true);
//        ResponseEntity<?> response = controller.stopRecording(request);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo(ResponseEntity.ok().build());
//    }

    @Test
    void checkActiveRecording_shouldReturnActive() {
        Recorder recorder = new Recorder(1, 1, null, null, 60);
        when(recordService.findActiveRecordByCameraId(anyInt())).thenReturn(recorder);

        ResponseEntity<?> response = controller.checkActiveRecording(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new SimpleEntry<>("active", true));
    }

    @Test
    void checkActiveRecording_shouldReturnInactive() {
        when(recordService.findActiveRecordByCameraId(anyInt())).thenReturn(null);

        ResponseEntity<?> response = controller.checkActiveRecording(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new SimpleEntry<>("active", false));
    }
}
