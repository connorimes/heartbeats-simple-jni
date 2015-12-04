package edu.uchicago.cs.heartbeats;

import java.nio.ByteBuffer;

/**
 * JNI bindings for native heartbeat implementation.
 * 
 * @author Connor Imes
 */
public final class HeartbeatPowJNI {
	private static HeartbeatPowJNI instance = null;

	/**
	 * Get an instance of {@link HeartbeatPowJNI}. On the first access, this
	 * method loads the native library. Failure to load will result in runtime
	 * exceptions.
	 * 
	 * @return {@link HeartbeatPowJNI}
	 */
	public static HeartbeatPowJNI get() {
		if (instance == null) {
			System.loadLibrary("hbs-pow-wrapper");
			instance = new HeartbeatPowJNI();
		}
		return instance;
	}

	public native ByteBuffer init(int windowSize, int logFd);

	public native void heartbeat(ByteBuffer ptr, long userTag, long work, long startTime, long endTime,
			long startEnergy, long endEnergy);

	public native void finish(ByteBuffer ptr);

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

	public native long getGlobalEnergy(ByteBuffer ptr);

	public native long getWindowEnergy(ByteBuffer ptr);

	public native double getGlobalPower(ByteBuffer ptr);

	public native double getWindowPower(ByteBuffer ptr);

	public native double getInstantPower(ByteBuffer ptr);
}
