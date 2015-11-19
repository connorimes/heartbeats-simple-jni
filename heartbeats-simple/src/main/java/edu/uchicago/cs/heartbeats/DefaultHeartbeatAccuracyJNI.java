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
public class DefaultHeartbeatAccuracyJNI extends AbstractDefaultHeartbeatJNI implements HeartbeatAccuracy {

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultHeartbeatAccuracyJNI(final ByteBuffer nativePtr) {
		this.nativePtr = nativePtr;
	}

	/**
	 * Create a {@link DefaultHeartbeatAccuracyJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatAccuracyJNI create(final int windowSize) {
		final ByteBuffer ptr = HeartbeatAccJNI.get().heartbeatAccInit(windowSize);
		if (ptr == null) {
			throw new IllegalStateException("Failed to get heartbeat over JNI");
		}
		return new DefaultHeartbeatAccuracyJNI(ptr);
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long accuracy) {
		enforceNotFinished();
		// TODO: enforce that values are unsigned.
		HeartbeatAccJNI.get().heartbeatAcc(nativePtr, userTag, work, startTime, endTime, accuracy);
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		heartbeat(userTag, work, startTime, endTime, 0);
	}

	public void finish() {
		enforceNotFinished();
		HeartbeatJNI.get().heartbeatFinish(nativePtr);
		nativePtr = null;
	}

	public void logHeader(final FileOutputStream fos) throws IOException {
		if (HeartbeatJNI.get().heartbeatLogHeader(getFileDescriptor(fos)) != 0) {
			throw new IOException("Failed to write log header");
		}
	}

	public void logWindowBuffer(final FileOutputStream fos) throws IOException {
		enforceNotFinished();
		if (HeartbeatJNI.get().heartbeatLogWindowBuffer(nativePtr, getFileDescriptor(fos)) != 0) {
			throw new IOException("Failed to write window buffer");
		}
	}

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

}
