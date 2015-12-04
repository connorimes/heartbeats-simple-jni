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
public class DefaultHeartbeatAccuracyPowerJNI extends AbstractDefaultHeartbeatJNI implements HeartbeatAccuracyPower {

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultHeartbeatAccuracyPowerJNI(final ByteBuffer nativePtr) {
		this(nativePtr, null);
	}

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 * @param logStream
	 */
	protected DefaultHeartbeatAccuracyPowerJNI(final ByteBuffer nativePtr, final FileOutputStream logStream) {
		super(nativePtr, logStream);
	}

	/**
	 * Create a {@link DefaultHeartbeatAccuracyPowerJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatAccuracyPowerJNI create(final int windowSize) {
		return create(windowSize, null);
	}

	/**
	 * Create a {@link DefaultHeartbeatAccuracyPowerJNI}.
	 * 
	 * @param windowSize
	 * @param logStream
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatAccuracyPowerJNI create(final int windowSize, final FileOutputStream logStream) {
		try {
			final ByteBuffer ptr = HeartbeatAccPowJNI.get().heartbeatAccPowInit(windowSize,
					getFileDescriptor(logStream));
			if (ptr == null) {
				throw new IllegalStateException("Failed to get heartbeat over JNI");
			}
			return new DefaultHeartbeatAccuracyPowerJNI(ptr, logStream);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to get file descriptor");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long accuracy, final long startEnergy, long endEnergy) {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			HeartbeatAccPowJNI.get().heartbeatAccPow(nativePtr, userTag, work, startTime, endTime, accuracy,
					startEnergy, endEnergy);
		} finally {
			lock.readLock().unlock();
		}
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

	protected void finishUnchecked() {
		HeartbeatAccPowJNI.get().heartbeatAccPowFinish(nativePtr);
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
				if (HeartbeatAccPowJNI.get().heartbeatAccPowLogHeader(getFileDescriptor(logStream)) != 0) {
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
				if (HeartbeatAccPowJNI.get().heartbeatAccPowLogWindowBuffer(nativePtr,
						getFileDescriptor(logStream)) != 0) {
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
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowSize(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getUserTag() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetUserTag(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetInstantPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalAccuracy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalAccuracy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowAccuracy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowAccuracy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalAccuracyRate() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalAccuracyRate(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowAccuracyRate() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowAccuracyRate(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantAccuracyRate() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetInstantAccuracyRate(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalEnergy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalEnergy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowEnergy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowEnergy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPower() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetGlobalPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPower() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetWindowPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPower() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccPowJNI.get().heartbeatAccPowGetInstantPower(nativePtr);
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
