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

	public native ByteBuffer heartbeatInit(int window_size);

	public native void heartbeat(ByteBuffer ptr, long userTag, long work, long startTime, long endTime);

	public native void heartbeatFinish(ByteBuffer ptr);

	public native int heartbeatLogHeader(int fd);

	public native int heartbeatLogWindowBuffer(ByteBuffer ptr, int fd);

	public native long heartbeatGetWindowSize(ByteBuffer ptr);

	public native long heartbeatGetUserTag(ByteBuffer ptr);

	public native long heartbeatGetGlobalTime(ByteBuffer ptr);

	public native long heartbeatGetWindowTime(ByteBuffer ptr);

	public native long heartbeatGetGlobalWork(ByteBuffer ptr);

	public native long heartbeatGetWindowWork(ByteBuffer ptr);

	public native double heartbeatGetGlobalPerf(ByteBuffer ptr);

	public native double heartbeatGetWindowPerf(ByteBuffer ptr);

	public native double heartbeatGetInstantPerf(ByteBuffer ptr);
}
