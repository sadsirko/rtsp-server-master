package com.rtsp.rtspserver.server;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RtspServerHandlerSymflowerTest {
	@Test
	public void extractURLWithoutId1() {
		String input = null;
		String expected = "Invalid URL";
		String actual = RtspServerHandler.extractURLWithoutId(input);

		assertEquals(expected, actual);
	}

	@Test
	public void extractURLWithoutId2() {
		String input = ":AD:";
		String expected = "Invalid URL";
		String actual = RtspServerHandler.extractURLWithoutId(input);

		assertEquals(expected, actual);
	}

	@Test
	public void extractURLWithoutId3() {
		String input = "A";
		String expected = "Invalid URL";
		String actual = RtspServerHandler.extractURLWithoutId(input);

		assertEquals(expected, actual);
	}
}
