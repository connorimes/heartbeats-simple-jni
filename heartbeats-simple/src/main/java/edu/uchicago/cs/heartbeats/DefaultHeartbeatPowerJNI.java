package edu.uchicago.cs.heartbeats;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Gets a heartbeat implementation and exposes methods for performing operations
 * on it. This implementation is a simple wrapper around the JNI functions.
 * 
 * Attempting to perform operations after {@link #finish()} is called will
 * result in an {@link IllegalStateException}.
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
		this(nativePtr, null);
	}

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 * @param logStream
	 */
	protected DefaultHeartbeatPowerJNI(final ByteBuffer nativePtr, final FileOutputStream logStream) {
		super(nativePtr, logStream);
	}

	/**
	 * Create a {@link DefaultHeartbeatPowerJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatPowerJNI create(final int windowSize) {
		return create(windowSize, null);
	}

	/**
	 * Create a {@link DefaultHeartbeatPowerJNI}.
	 * 
	 * @param windowSize
	 * @param logStream
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatPowerJNI create(final int windowSize, final FileOutputStream logStream) {
		try {
			final ByteBuffer ptr = HeartbeatPowJNI.get().heartbeatPowInit(windowSize, getFileDescriptor(logStream));
			if (ptr == null) {
				throw new IllegalStateException("Failed to get heartbeat over JNI");
			}
			return new DefaultHeartbeatPowerJNI(ptr, logStream);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to get file descriptor");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long startEnergy, final long endEnergy) {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			HeartbeatPowJNI.get().heartbeatPow(nativePtr, userTag, work, startTime, endTime, startEnergy, endEnergy);
		} finally {
			lock.readLock().unlock();
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		heartbeat(userTag, work, startTime, endTime, 0, 0);
	}

	protected void finishUnchecked() {
		HeartbeatPowJNI.get().heartbeatPowFinish(nativePtr);
		nativePtr = null;
	}

	public void finish() {
		try {
			lock.writeLock().lock();
			enforceNotFinished();
			finishUnchecked();
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void logHeader() throws IOException {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			if (logStream != null) {
				if (HeartbeatPowJNI.get().heartbeatPowLogHeader(getFileDescriptor(logStream)) != 0) {
					throw new IOException("Failed to write log header");
				}
			}
		} finally {
			lock.readLock().unlock();
		}
	}

	public void logWindowBuffer() throws IOException {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			if (logStream != null) {
				if (HeartbeatPowJNI.get().heartbeatPowLogWindowBuffer(nativePtr, getFileDescriptor(logStream)) != 0) {
					throw new IOException("Failed to write window buffer");
				}
			}
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowSize() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetWindowSize(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getUserTag() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetUserTag(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetGlobalTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetWindowTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetGlobalWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetWindowWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetGlobalPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetWindowPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetInstantPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalEnergy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetGlobalEnergy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowEnergy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetWindowEnergy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPower() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetGlobalPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPower() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetWindowPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPower() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().heartbeatPowGetInstantPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// last-ditch effort to cleanup if user didn't follow protocol
		try {
			if (nativePtr != null) {
				finishUnchecked();
			}
		} finally {
			super.finalize();
		}
	}

}
