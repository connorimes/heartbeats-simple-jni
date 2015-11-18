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
public class DefaultHeartbeatPowerJNI implements HeartbeatPower {
	private volatile ByteBuffer nativePtr;

	/**
	 * Create a {@link DefaultHeartbeatPowerJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public DefaultHeartbeatPowerJNI(final int windowSize) {
		nativePtr = HeartbeatPowJNI.get().heartbeatPowInit(windowSize);
		if (nativePtr == null) {
			throw new IllegalStateException("Failed to get heartbeat over JNI");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long startEnergy, final long endEnergy) {
		enforceNotFinished();
		// TODO: enforce that values are unsigned.
		if (HeartbeatPowJNI.get().heartbeatPow(nativePtr, userTag, work, startTime, endTime, startEnergy,
				endEnergy) != 0) {
			throw new IllegalArgumentException("Failed to issue heartbeat");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		heartbeat(userTag, work, startTime, endTime, 0, 0);
	}

	public int finish() {
		enforceNotFinished();
		int result = HeartbeatPowJNI.get().heartbeatPowFinish(nativePtr);
		nativePtr = null;
		return result;
	}

	// public int logHeader(int fd);

	// public int logwindowBuffer(int fd);

	public long getWindowSize() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetWindowSize(nativePtr);
	}

	public long getUserTag() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetUserTag(nativePtr);
	}

	public long getGlobalTime() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetGlobalTime(nativePtr);
	}

	public long getWindowTime() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetWindowTime(nativePtr);
	}

	public long getGlobalWork() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetGlobalWork(nativePtr);
	}

	public long getWindowWork() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetWindowWork(nativePtr);
	}

	public double getGlobalPerf() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetGlobalPerf(nativePtr);
	}

	public double getWindowPerf() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetWindowPerf(nativePtr);
	}

	public double getInstantPerf() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetInstantPerf(nativePtr);
	}

	public long getGlobalEnergy() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetGlobalEnergy(nativePtr);
	}

	public long getWindowEnergy() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetWindowEnergy(nativePtr);
	}

	public double getGlobalPower() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetGlobalPower(nativePtr);
	}

	public double getWindowPower() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetWindowPower(nativePtr);
	}

	public double getInstantPower() {
		enforceNotFinished();
		return HeartbeatPowJNI.get().heartbeatPowGetInstantPower(nativePtr);
	}

	private void enforceNotFinished() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
	}

}
