package com.rtsp.rtspserver.service;

import com.rtsp.rtspserver.model.Camera;
import com.rtsp.rtspserver.repository.CameraRepository;
import com.rtsp.rtspserver.server.events.CameraAddedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CameraServiceTest {

	@Mock
	private CameraRepository cameraRepository;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@InjectMocks
	private CameraService cameraService;

	@Test
	void addCamera_ShouldCallRepository_AndPublishEvent() {
		Camera camera = new Camera(1, "Test Camera", "rtsp://test.url:554", 1);
		when(cameraRepository.addCamera(any(Camera.class))).thenReturn(camera);

		Camera savedCamera = cameraService.addCamera(camera);

		assertNotNull(savedCamera);
		assertEquals(camera, savedCamera);
		verify(cameraRepository).addCamera(camera);
		verify(eventPublisher).publishEvent(any(CameraAddedEvent.class));
	}
//	@Test
//	void deleteCamera_ShouldCallRepository_AndReturnTrue() {
//		Camera camera = new Camera(1, "Test Camera", "rtsp://test.url:554", 1);
//		when(cameraRepository.findById(1)).thenReturn(camera);
//		when(cameraRepository.delete(1)).thenReturn(true);
//
//		assertTrue(cameraService.deleteCamera(1));
//		verify(cameraRepository).delete(1);
//	}

}
