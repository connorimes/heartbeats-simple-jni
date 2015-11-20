package edu.uchicago.cs.heartbeats;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Gets a heartbeat implementation and exposes methods for performing operations
 * on it. This implementation is a simple wrapper around the JNI functions.
 * 
 * This implementation is <b>NOT</b> thread safe and should be synchronized
 * externally. Attempting to perform operations after {@link #finish()} is
 * called will result in an {@link IllegalStateException}.
 * 
 * @author Connor Imes
 */
public class DefaultHeartbeatAccuracyPowerJNI extends AbstractDefaultHeartbeatJNI implements HeartbeatAccuracyPower {

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultHeartbeatAccuracyPowerJNI(final ByteBuffer nativePtr) {
		this.nativePtr = nativePtr;
	}

	/**
	 * Create a {@link DefaultHeartbeatAccuracyPowerJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatAccuracyPowerJNI create(final int windowSize) {
		final ByteBuffer ptr = HeartbeatAccPowJNI.get().heartbeatAccPowInit(windowSize);
		if (ptr == null) {
			throw new IllegalStateException("Failed to get heartbeat over JNI");
		}
		return new DefaultHeartbeatAccuracyPowerJNI(ptr);
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long accuracy, final long startEnergy, long endEnergy) {
		enforceNotFinished();
		HeartbeatAccPowJNI.get().heartbeatAccPow(nativePtr, userTag, work, startTime, endTime, accuracy, startEnergy,
				endEnergy);
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long startEnergy, final long endEnergy) {
		heartbeat(userTag, work, startTime, endTime, 0, startEnergy, endEnergy);
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long accuracy) {
		heartbeat(userTag, work, startTime, endTime, accuracy, 0, 0);
	}

	public void heartbeat(long userTag, long work, long startTime, long endTime) {
		heartbeat(userTag, work, startTime, endTime, 0, 0, 0);
	}

	public void finish() {
		enforceNotFinished();
		HeartbeatAccPowJNI.get().heartbeatAccPowFinish(nativePtr);
		nativePtr = null;
	}

	public void logHeader(final FileOutputStream fos) throws IOException {
		if (HeartbeatAccPowJNI.get().heartbeatAccPowLogHeader(getFileDescriptor(fos)) != 0) {
			throw new IOException("Failed to write log header");
		}
	}

	public void logWindowBuffer(final FileOutputStream fos) throws IOException {
		enforceNotFinished();
		if (HeartbeatAccPowJNI.get().heartbeatAccPowLogWindowBuffer(nativePtr, getFileDescriptor(fos)) != 0) {
			throw new IOException("Failed to write window buffer");
		}
	}

	public long getWindowSize() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowSize(nativePtr);
	}

	public long getUserTag() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetUserTag(nativePtr);
	}

	public long getGlobalTime() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalTime(nativePtr);
	}

	public long getWindowTime() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowTime(nativePtr);
	}

	public long getGlobalWork() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalWork(nativePtr);
	}

	public long getWindowWork() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowWork(nativePtr);
	}

	public double getGlobalPerf() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalPerf(nativePtr);
	}

	public double getWindowPerf() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowPerf(nativePtr);
	}

	public double getInstantPerf() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetInstantPerf(nativePtr);
	}

	public long getGlobalAccuracy() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalAccuracy(nativePtr);
	}

	public long getWindowAccuracy() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowAccuracy(nativePtr);
	}

	public double getGlobalAccuracyRate() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalAccuracyRate(nativePtr);
	}

	public double getWindowAccuracyRate() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowAccuracyRate(nativePtr);
	}

	public double getInstantAccuracyRate() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetInstantAccuracyRate(nativePtr);
	}

	public long getGlobalEnergy() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalEnergy(nativePtr);
	}

	public long getWindowEnergy() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowEnergy(nativePtr);
	}

	public double getGlobalPower() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalPower(nativePtr);
	}

	public double getWindowPower() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowPower(nativePtr);
	}

	public double getInstantPower() {
		enforceNotFinished();
		return HeartbeatAccPowJNI.get().heartbeatAccPowGetInstantPower(nativePtr);
	}

}
