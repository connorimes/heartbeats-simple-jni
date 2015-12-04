package edu.uchicago.cs.heartbeats;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Gets a heartbeat implementation and exposes methods for performing operations
 * on it. This implementation is a simple wrapper around the JNI functions.
 * 
 * Attempting to perform operations after {@link #dispose()} is called will
 * result in an {@link IllegalStateException}.
 * 
 * @author Connor Imes
 */
public class DefaultHeartbeatAccuracyPower extends AbstractDefaultHeartbeat implements HeartbeatAccuracyPower {

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultHeartbeatAccuracyPower(final ByteBuffer nativePtr) {
		this(nativePtr, null);
	}

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 * @param logStream
	 */
	protected DefaultHeartbeatAccuracyPower(final ByteBuffer nativePtr, final FileOutputStream logStream) {
		super(nativePtr, logStream);
	}

	/**
	 * Create a {@link DefaultHeartbeatAccuracyPower}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatAccuracyPower create(final int windowSize) {
		return create(windowSize, null);
	}

	/**
	 * Create a {@link DefaultHeartbeatAccuracyPower}.
	 * 
	 * @param windowSize
	 * @param logStream
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatAccuracyPower create(final int windowSize, final FileOutputStream logStream) {
		try {
			final ByteBuffer ptr = HeartbeatAccPowJNI.get().init(windowSize, getFileDescriptor(logStream));
			if (ptr == null) {
				throw new IllegalStateException("Failed to get heartbeat over JNI");
			}
			return new DefaultHeartbeatAccuracyPower(ptr, logStream);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to get file descriptor");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long accuracy, final long startEnergy, long endEnergy) {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			HeartbeatAccPowJNI.get().heartbeat(nativePtr, userTag, work, startTime, endTime, accuracy,
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

	protected void free() {
		HeartbeatAccPowJNI.get().free(nativePtr);
		nativePtr = null;
	}

	public void dispose() {
		try {
			lock.writeLock().lock();
			enforceNotDisposed();
			free();
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void logHeader() throws IOException {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			if (logStream != null) {
				if (HeartbeatAccPowJNI.get().logHeader(getFileDescriptor(logStream)) != 0) {
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
			enforceNotDisposed();
			if (logStream != null) {
				if (HeartbeatAccPowJNI.get().logWindowBuffer(nativePtr, getFileDescriptor(logStream)) != 0) {
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
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getWindowSize(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getUserTag() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getUserTag(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalTime() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getGlobalTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowTime() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getWindowTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalWork() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getGlobalWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowWork() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getWindowWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPerf() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getGlobalPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPerf() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getWindowPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPerf() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getInstantPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalAccuracy() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getGlobalAccuracy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowAccuracy() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getWindowAccuracy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalAccuracyRate() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getGlobalAccuracyRate(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowAccuracyRate() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getWindowAccuracyRate(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantAccuracyRate() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getInstantAccuracyRate(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalEnergy() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getGlobalEnergy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowEnergy() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getWindowEnergy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPower() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getGlobalPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPower() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getWindowPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPower() {
		try {
			lock.readLock().lock();
			enforceNotDisposed();
			return HeartbeatAccPowJNI.get().getInstantPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// last-ditch effort to cleanup if user didn't follow protocol
		try {
			if (nativePtr != null) {
				free();
			}
		} finally {
			super.finalize();
		}
	}

}
