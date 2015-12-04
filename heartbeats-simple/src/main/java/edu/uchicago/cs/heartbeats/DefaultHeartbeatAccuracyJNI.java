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
public class DefaultHeartbeatAccuracyJNI extends AbstractDefaultHeartbeatJNI implements HeartbeatAccuracy {

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultHeartbeatAccuracyJNI(final ByteBuffer nativePtr) {
		this(nativePtr, null);
	}

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 * @param logStream
	 */
	protected DefaultHeartbeatAccuracyJNI(final ByteBuffer nativePtr, final FileOutputStream logStream) {
		super(nativePtr, logStream);
	}

	/**
	 * Create a {@link DefaultHeartbeatAccuracyJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatAccuracyJNI create(final int windowSize) {
		return create(windowSize, null);
	}

	/**
	 * Create a {@link DefaultHeartbeatAccuracyJNI}.
	 * 
	 * @param windowSize
	 * @param logStream
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatAccuracyJNI create(final int windowSize, final FileOutputStream logStream) {
		try {
			final ByteBuffer ptr = HeartbeatAccJNI.get().heartbeatAccInit(windowSize, getFileDescriptor(logStream));
			if (ptr == null) {
				throw new IllegalStateException("Failed to get heartbeat over JNI");
			}
			return new DefaultHeartbeatAccuracyJNI(ptr, logStream);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to get file descriptor");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long accuracy) {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			HeartbeatAccJNI.get().heartbeatAcc(nativePtr, userTag, work, startTime, endTime, accuracy);
		} finally {
			lock.readLock().unlock();
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		heartbeat(userTag, work, startTime, endTime, 0);
	}

	protected void finishUnchecked() {
		HeartbeatAccJNI.get().heartbeatAccFinish(nativePtr);
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
				if (HeartbeatAccJNI.get().heartbeatAccLogHeader(getFileDescriptor(logStream)) != 0) {
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
				if (HeartbeatAccJNI.get().heartbeatAccLogWindowBuffer(nativePtr, getFileDescriptor(logStream)) != 0) {
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
			return HeartbeatAccJNI.get().heartbeatAccGetWindowSize(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getUserTag() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetUserTag(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetGlobalTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetWindowTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetGlobalWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetWindowWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetGlobalPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetWindowPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetInstantPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalAccuracy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetGlobalAccuracy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowAccuracy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetWindowAccuracy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalAccuracyRate() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetGlobalAccuracyRate(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowAccuracyRate() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetWindowAccuracyRate(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantAccuracyRate() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatAccJNI.get().heartbeatAccGetInstantAccuracyRate(nativePtr);
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
