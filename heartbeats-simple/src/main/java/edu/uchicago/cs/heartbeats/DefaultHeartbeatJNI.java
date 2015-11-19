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
public class DefaultHeartbeatJNI extends AbstractDefaultHeartbeatJNI implements Heartbeat {

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultHeartbeatJNI(final ByteBuffer nativePtr) {
		this.nativePtr = nativePtr;
	}

	/**
	 * Create a {@link DefaultHeartbeatJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatJNI create(final int windowSize) {
		final ByteBuffer ptr = HeartbeatJNI.get().heartbeatInit(windowSize);
		if (ptr == null) {
			throw new IllegalStateException("Failed to get heartbeat over JNI");
		}
		return new DefaultHeartbeatJNI(ptr);
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		enforceNotFinished();
		// TODO: enforce that values are unsigned.
		HeartbeatJNI.get().heartbeat(nativePtr, userTag, work, startTime, endTime);
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

}
