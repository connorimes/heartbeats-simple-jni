package edu.uchicago.cs.heartbeats;

import java.nio.ByteBuffer;

/**
 * Gets a heartbeat implementation and exposes methods for performing operations
 * on it. This implementation is a simple wrapper around the JNI functions.
 * 
 * This implementation is <b>NOT</b> thread safe and should be synchronized
 * externally. Attempting to perform operations after {@link #finish()} is
 * called will result in an {@link IllegalStateException}.
 * 
 * Failure to allocate the native resources also results in an
 * {@link IllegalStateException} in the constructor.
 * 
 * @author Connor Imes
 */
public class DefaultHeartbeatAccuracyJNI implements HeartbeatAccuracy {
	private volatile ByteBuffer nativePtr;

	/**
	 * Create a {@link DefaultHeartbeatAccuracyJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public DefaultHeartbeatAccuracyJNI(final int windowSize) {
		nativePtr = HeartbeatAccJNI.get().heartbeatAccInit(windowSize);
		if (nativePtr == null) {
			throw new IllegalStateException("Failed to get heartbeat over JNI");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long accuracy) {
		enforceNotFinished();
		// TODO: enforce that values are unsigned.
		if (HeartbeatAccJNI.get().heartbeatAcc(nativePtr, userTag, work, startTime, endTime, accuracy) != 0) {
			throw new IllegalArgumentException("Failed to issue heartbeat");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		heartbeat(userTag, work, startTime, endTime, 0);
	}

	public int finish() {
		enforceNotFinished();
		int result = HeartbeatAccJNI.get().heartbeatAccFinish(nativePtr);
		nativePtr = null;
		return result;
	}

	// public int logHeader(int fd);

	// public int logwindowBuffer(int fd);

	public long getWindowSize() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetWindowSize(nativePtr);
	}

	public long getUserTag() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetUserTag(nativePtr);
	}

	public long getGlobalTime() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetGlobalTime(nativePtr);
	}

	public long getWindowTime() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetWindowTime(nativePtr);
	}

	public long getGlobalWork() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetGlobalWork(nativePtr);
	}

	public long getWindowWork() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetWindowWork(nativePtr);
	}

	public double getGlobalPerf() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetGlobalPerf(nativePtr);
	}

	public double getWindowPerf() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetWindowPerf(nativePtr);
	}

	public double getInstantPerf() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetInstantPerf(nativePtr);
	}

	public long getGlobalAccuracy() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetGlobalAccuracy(nativePtr);
	}

	public long getWindowAccuracy() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetWindowAccuracy(nativePtr);
	}

	public double getGlobalAccuracyRate() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetGlobalAccuracyRate(nativePtr);
	}

	public double getWindowAccuracyRate() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetWindowAccuracyRate(nativePtr);
	}

	public double getInstantAccuracyRate() {
		enforceNotFinished();
		return HeartbeatAccJNI.get().heartbeatAccGetInstantAccuracyRate(nativePtr);
	}

	private void enforceNotFinished() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
	}

}
