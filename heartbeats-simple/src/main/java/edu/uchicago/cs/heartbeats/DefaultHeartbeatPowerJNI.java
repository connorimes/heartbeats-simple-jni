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
public class DefaultHeartbeatPowerJNI extends AbstractDefaultHeartbeatJNI implements HeartbeatPower {

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultHeartbeatPowerJNI(final ByteBuffer nativePtr) {
		this.nativePtr = nativePtr;
	}

	/**
	 * Create a {@link DefaultHeartbeatPowerJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatPowerJNI create(final int windowSize) {
		final ByteBuffer ptr = HeartbeatPowJNI.get().heartbeatPowInit(windowSize);
		if (ptr == null) {
			throw new IllegalStateException("Failed to get heartbeat over JNI");
		}
		return new DefaultHeartbeatPowerJNI(ptr);
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long startEnergy, final long endEnergy) {
		enforceNotFinished();
		// TODO: enforce that values are unsigned.
		HeartbeatPowJNI.get().heartbeatPow(nativePtr, userTag, work, startTime, endTime, startEnergy, endEnergy);
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		heartbeat(userTag, work, startTime, endTime, 0, 0);
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

}
