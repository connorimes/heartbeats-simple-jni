package edu.uchicago.cs.heartbeats;

import java.nio.ByteBuffer;

/**
 * JNI bindings for native heartbeat implementation.
 * 
 * @author Connor Imes
 */
public final class HeartbeatAccJNI {
	private static HeartbeatAccJNI instance = null;

	/**
	 * Get an instance of {@link HeartbeatAccJNI}. On the first access, this
	 * method loads the native library. Failure to load will result in runtime
	 * exceptions.
	 * 
	 * @return {@link HeartbeatAccJNI}
	 */
	public static HeartbeatAccJNI get() {
		if (instance == null) {
			System.loadLibrary("hbs-acc-wrapper");
			instance = new HeartbeatAccJNI();
		}
		return instance;
	}

	public native ByteBuffer heartbeatAccInit(int windowSize, int logFd);

	public native void heartbeatAcc(ByteBuffer ptr, long userTag, long work, long startTime, long endTime,
			long accuracy);

	public native void heartbeatAccFinish(ByteBuffer ptr);

	public native int heartbeatAccLogHeader(int fd);

	public native int heartbeatAccLogWindowBuffer(ByteBuffer ptr, int fd);

	public native long heartbeatAccGetWindowSize(ByteBuffer ptr);

	public native long heartbeatAccGetUserTag(ByteBuffer ptr);

	public native long heartbeatAccGetGlobalTime(ByteBuffer ptr);

	public native long heartbeatAccGetWindowTime(ByteBuffer ptr);

	public native long heartbeatAccGetGlobalWork(ByteBuffer ptr);

	public native long heartbeatAccGetWindowWork(ByteBuffer ptr);

	public native double heartbeatAccGetGlobalPerf(ByteBuffer ptr);

	public native double heartbeatAccGetWindowPerf(ByteBuffer ptr);

	public native double heartbeatAccGetInstantPerf(ByteBuffer ptr);

	public native long heartbeatAccGetGlobalAccuracy(ByteBuffer ptr);

	public native long heartbeatAccGetWindowAccuracy(ByteBuffer ptr);

	public native double heartbeatAccGetGlobalAccuracyRate(ByteBuffer ptr);

	public native double heartbeatAccGetWindowAccuracyRate(ByteBuffer ptr);

	public native double heartbeatAccGetInstantAccuracyRate(ByteBuffer ptr);
}
