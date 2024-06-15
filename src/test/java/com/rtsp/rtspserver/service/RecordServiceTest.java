package com.rtsp.rtspserver.service;

import com.rtsp.rtspserver.model.Recorder;
import com.rtsp.rtspserver.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordService recordService;

    @Test
    void startRecording_ShouldThrowException_WhenDurationIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            recordService.startRecording(1, 0);
        }, "Duration must be between 1 and 30 minutes.");

        assertThrows(IllegalArgumentException.class, () -> {
            recordService.startRecording(1, 31);
        }, "Duration must be between 1 and 30 minutes.");
    }

    @Test
    void startRecording_ShouldNotThrow_WhenDurationIsValid() {
        Recorder recorder = new Recorder(1, 1, null, null, 15);
        when(recordRepository.addRecord(any(Recorder.class))).thenReturn(recorder);

        assertDoesNotThrow(() -> {
            Recorder result = recordService.startRecording(1, 15);
            assertNotNull(result);
        });
    }
}
