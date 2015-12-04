package edu.uchicago.cs.heartbeats;

import static org.junit.Assert.*;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

/**
 * JUnit tests for {@link DefaultHeartbeat}.
 * 
 * @author Connor Imes
 */
public class DefaultHeartbeatTest {
	private static final int WINDOW_SIZE = 20;

	@Test
	public void test_normal() {
		final long userTag = 0;
		final long endTime = 1000000000;
		final long work = 1;
		FileOutputStream fos = new FileOutputStream(FileDescriptor.out);
		Heartbeat hb = DefaultHeartbeat.create(WINDOW_SIZE, fos);
		hb.heartbeat(userTag, work, 0, endTime);
		assertEquals("getWindowSize", WINDOW_SIZE, hb.getWindowSize());
		assertEquals("getUserTag", userTag, hb.getUserTag());
		assertEquals("getGlobalTime", endTime, hb.getGlobalTime());
		assertEquals("getWindowTime", endTime, hb.getWindowTime());
		assertEquals("getGlobalWork", work, hb.getGlobalWork());
		assertEquals("getWindowWork", work, hb.getWindowWork());
		try {
			hb.logHeader();
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException logging header");
		}
		try {
			hb.logWindowBuffer();
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException logging window buffer");
		}
		// double values are hard to test - just verify the methods don't fail
		assertTrue("getGlobalPerf", hb.getGlobalPerf() > 0);
		assertTrue("getWindowPerf", hb.getWindowPerf() > 0);
		assertTrue("getInstantPerf", hb.getInstantPerf() > 0);
		hb.finish();
	}

	@Test(expected = IllegalStateException.class)
	public void test_access_after_finish() {
		Heartbeat hb = DefaultHeartbeat.create(WINDOW_SIZE, null);
		hb.finish();
		// too cumbersome to try all methods, just do one
		hb.getWindowSize();
	}

}
