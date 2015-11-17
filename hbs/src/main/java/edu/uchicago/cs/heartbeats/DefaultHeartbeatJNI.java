package edu.uchicago.cs.heartbeats;

import java.math.BigInteger;
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
	 * 
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public DefaultHeartbeatJNI(final int windowSize) {
		nativePtr = HeartbeatJNI.get().heartbeatInit(windowSize);
		if (nativePtr == null) {
			throw new IllegalStateException("Failed to get heartbeat over JNI");
		}
	}

	public void heartbeat(long userTag, long work, long startTime, long endTime) {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		// TODO: enforce that values are unsigned.
		if (HeartbeatJNI.get().heartbeat(nativePtr, userTag, work, startTime, endTime) != 0) {
			throw new IllegalArgumentException("Failed to issue heartbeat");
		}
	}

	public int finish() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
		int result = HeartbeatJNI.get().heartbeatFinish(nativePtr);
		nativePtr = null;
		return result;
	}

	// public int logHeader(int fd);

	// public int logwindowBuffer(int fd);

	public long getWindowSize() {
		// TODO
		return 0;
	}

	public long getUserTag() {
		// TODO
		return 0;
	}

	public long getGlobalTime() {
		// TODO
		return 0;
	}

	public long getWindowTime() {
		// TODO
		return 0;
	}

	public long getGlobalWork() {
		// TODO
		return 0;
	}

	public long getWindowWork() {
		// TODO
		return 0;
	}

	public double getGlobalPerf() {
		// TODO
		return 0;
	}

	public double getWindowPerf() {
		// TODO
		return 0;
	}

	public double getInstantPerf() {
		// TODO
		return 0;
	}

}
