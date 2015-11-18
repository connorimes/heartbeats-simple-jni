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

	public native ByteBuffer heartbeatPowInit(int window_size);

	public native int heartbeatPow(ByteBuffer ptr, long userTag, long work, long startTime, long endTime,
			long start_energy, long end_energy);

	public native int heartbeatPowFinish(ByteBuffer ptr);

	public native int heartbeatPowLogHeader(int fd);

	public native int heartbeatPowLogWindowBuffer(ByteBuffer ptr, int fd);

	public native long heartbeatPowGetWindowSize(ByteBuffer ptr);

	public native long heartbeatPowGetUserTag(ByteBuffer ptr);

	public native long heartbeatPowGetGlobalTime(ByteBuffer ptr);

	public native long heartbeatPowGetWindowTime(ByteBuffer ptr);

	public native long heartbeatPowGetGlobalWork(ByteBuffer ptr);

	public native long heartbeatPowGetWindowWork(ByteBuffer ptr);

	public native double heartbeatPowGetGlobalPerf(ByteBuffer ptr);

	public native double heartbeatPowGetWindowPerf(ByteBuffer ptr);

	public native double heartbeatPowGetInstantPerf(ByteBuffer ptr);

	public native long heartbeatPowGetGlobalEnergy(ByteBuffer ptr);

	public native long heartbeatPowGetWindowEnergy(ByteBuffer ptr);

	public native double heartbeatPowGetGlobalPower(ByteBuffer ptr);

	public native double heartbeatPowGetWindowPower(ByteBuffer ptr);

	public native double heartbeatPowGetInstantPower(ByteBuffer ptr);
}
