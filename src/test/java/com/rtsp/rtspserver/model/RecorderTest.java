package com.rtsp.rtspserver.model;

import java.sql.Timestamp;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RecorderTest {
	@Test
	public void Recorder() {
		int cameraId = 123;
		Timestamp startTime = null;
		Timestamp endTime = null;
		int duration = 123;
		Recorder expected = new Recorder(123, null, null, 123);
		Recorder actual = new Recorder(cameraId, startTime, endTime, duration);

		assertEquals(expected, actual);
	}
}
