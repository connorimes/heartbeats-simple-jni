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

	public native ByteBuffer heartbeatAccPowInit(int window_size);

	public native int heartbeatAccPow(ByteBuffer ptr, long userTag, long work, long startTime, long endTime,
			long accuracy, long start_energy, long end_energy);

	public native int heartbeatAccPowFinish(ByteBuffer ptr);

	public native int heartbeatAccPowLogHeader(int fd);

	public native int heartbeatAccPowLogWindowBuffer(ByteBuffer ptr, int fd);

	public native long heartbeatAccPowGetWindowSize(ByteBuffer ptr);

	public native long heartbeatAccPowGetUserTag(ByteBuffer ptr);

	public native long heartbeatAccPowGetGlobalTime(ByteBuffer ptr);

	public native long heartbeatAccPowGetWindowTime(ByteBuffer ptr);

	public native long heartbeatAccPowGetGlobalWork(ByteBuffer ptr);

	public native long heartbeatAccPowGetWindowWork(ByteBuffer ptr);

	public native double heartbeatAccPowGetGlobalPerf(ByteBuffer ptr);

	public native double heartbeatAccPowGetWindowPerf(ByteBuffer ptr);

	public native double heartbeatAccPowGetInstantPerf(ByteBuffer ptr);

	public native long heartbeatAccPowGetGlobalAccuracy(ByteBuffer ptr);

	public native long heartbeatAccPowGetWindowAccuracy(ByteBuffer ptr);

	public native double heartbeatAccPowGetGlobalAccuracyRate(ByteBuffer ptr);

	public native double heartbeatAccPowGetWindowAccuracyRate(ByteBuffer ptr);

	public native double heartbeatAccPowGetInstantAccuracyRate(ByteBuffer ptr);

	public native long heartbeatAccPowGetGlobalEnergy(ByteBuffer ptr);

	public native long heartbeatAccPowGetWindowEnergy(ByteBuffer ptr);

	public native double heartbeatAccPowGetGlobalPower(ByteBuffer ptr);

	public native double heartbeatAccPowGetWindowPower(ByteBuffer ptr);

	public native double heartbeatAccPowGetInstantPower(ByteBuffer ptr);
}
