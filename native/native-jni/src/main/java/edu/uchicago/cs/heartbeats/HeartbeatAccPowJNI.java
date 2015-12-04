package edu.uchicago.cs.heartbeats;

import java.nio.ByteBuffer;

/**
 * JNI bindings for native heartbeat implementation.
 * 
 * @author Connor Imes
 */
public final class HeartbeatAccPowJNI {
	private static HeartbeatAccPowJNI instance = null;

	/**
	 * Get an instance of {@link HeartbeatAccPowJNI}. On the first access, this
	 * method loads the native library. Failure to load will result in runtime
	 * exceptions.
	 * 
	 * @return {@link HeartbeatAccPowJNI}
	 */
	public static HeartbeatAccPowJNI get() {
		if (instance == null) {
			System.loadLibrary("hbs-acc-pow-wrapper");
			instance = new HeartbeatAccPowJNI();
		}
		return instance;
	}

	public native ByteBuffer init(int windowSize, int logFd);

	public native void heartbeat(ByteBuffer ptr, long userTag, long work, long startTime, long endTime,
			long accuracy, long startEnergy, long endEnergy);

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

	public native long getGlobalAccuracy(ByteBuffer ptr);

	public native long getWindowAccuracy(ByteBuffer ptr);

	public native double getGlobalAccuracyRate(ByteBuffer ptr);

	public native double getWindowAccuracyRate(ByteBuffer ptr);

	public native double getInstantAccuracyRate(ByteBuffer ptr);

	public native long getGlobalEnergy(ByteBuffer ptr);

	public native long getWindowEnergy(ByteBuffer ptr);

	public native double getGlobalPower(ByteBuffer ptr);

	public native double getWindowPower(ByteBuffer ptr);

	public native double getInstantPower(ByteBuffer ptr);
}
