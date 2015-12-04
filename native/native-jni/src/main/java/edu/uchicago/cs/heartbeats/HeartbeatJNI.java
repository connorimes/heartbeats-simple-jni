package edu.uchicago.cs.heartbeats;

import java.nio.ByteBuffer;

/**
 * JNI bindings for native heartbeat implementation.
 * 
 * @author Connor Imes
 */
public final class HeartbeatJNI {
	private static HeartbeatJNI instance = null;

	/**
	 * Get an instance of {@link HeartbeatJNI}. On the first access, this method
	 * loads the native library. Failure to load will result in runtime
	 * exceptions.
	 * 
	 * @return {@link HeartbeatJNI}
	 */
	public static HeartbeatJNI get() {
		if (instance == null) {
			System.loadLibrary("hbs-wrapper");
			instance = new HeartbeatJNI();
		}
		return instance;
	}

	public native ByteBuffer init(int windowSize, int logFd);

	public native void heartbeat(ByteBuffer ptr, long userTag, long work, long startTime, long endTime);

	public native void free(ByteBuffer ptr);

	public native int logHeader(int fd);

	public native int logWindowBuffer(ByteBuffer ptr, int fd);

	public native long getWindowSize(ByteBuffer ptr);

	public native long getUserTag(ByteBuffer ptr);

	public native long getGlobalTime(ByteBuffer ptr);

	public native long getWindowTime(ByteBuffer ptr);

	public native long getGlobalWork(ByteBuffer ptr);

	public native long getWindowWork(ByteBuffer ptr);

	public native double getGlobalPerf(ByteBuffer ptr);

	public native double getWindowPerf(ByteBuffer ptr);

	public native double getInstantPerf(ByteBuffer ptr);
}
