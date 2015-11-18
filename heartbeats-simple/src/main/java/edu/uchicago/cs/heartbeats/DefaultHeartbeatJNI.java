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
public class DefaultHeartbeatJNI implements Heartbeat {
	private volatile ByteBuffer nativePtr;

	/**
	 * Create a {@link DefaultHeartbeatJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public DefaultHeartbeatJNI(final int windowSize) {
		nativePtr = HeartbeatJNI.get().heartbeatInit(windowSize);
		if (nativePtr == null) {
			throw new IllegalStateException("Failed to get heartbeat over JNI");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		enforceNotFinished();
		// TODO: enforce that values are unsigned.
		if (HeartbeatJNI.get().heartbeat(nativePtr, userTag, work, startTime, endTime) != 0) {
			throw new IllegalArgumentException("Failed to issue heartbeat");
		}
	}

	public int finish() {
		enforceNotFinished();
		int result = HeartbeatJNI.get().heartbeatFinish(nativePtr);
		nativePtr = null;
		return result;
	}

	// public int logHeader(int fd);

	// public int logwindowBuffer(int fd);

	public long getWindowSize() {
		enforceNotFinished();
		return HeartbeatJNI.get().heartbeatGetWindowSize(nativePtr);
	}

	public long getUserTag() {
		enforceNotFinished();
		return HeartbeatJNI.get().heartbeatGetUserTag(nativePtr);
	}

	public long getGlobalTime() {
		enforceNotFinished();
		return HeartbeatJNI.get().heartbeatGetGlobalTime(nativePtr);
	}

	public long getWindowTime() {
		enforceNotFinished();
		return HeartbeatJNI.get().heartbeatGetWindowTime(nativePtr);
	}

	public long getGlobalWork() {
		enforceNotFinished();
		return HeartbeatJNI.get().heartbeatGetGlobalWork(nativePtr);
	}

	public long getWindowWork() {
		enforceNotFinished();
		return HeartbeatJNI.get().heartbeatGetWindowWork(nativePtr);
	}

	public double getGlobalPerf() {
		enforceNotFinished();
		return HeartbeatJNI.get().heartbeatGetGlobalPerf(nativePtr);
	}

	public double getWindowPerf() {
		enforceNotFinished();
		return HeartbeatJNI.get().heartbeatGetWindowPerf(nativePtr);
	}

	public double getInstantPerf() {
		enforceNotFinished();
		return HeartbeatJNI.get().heartbeatGetInstantPerf(nativePtr);
	}

	private void enforceNotFinished() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
	}

}
