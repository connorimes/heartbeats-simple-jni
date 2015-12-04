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
public class DefaultHeartbeatPower extends AbstractDefaultHeartbeat implements HeartbeatPower {

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 */
	protected DefaultHeartbeatPower(final ByteBuffer nativePtr) {
		this(nativePtr, null);
	}

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 * @param logStream
	 */
	protected DefaultHeartbeatPower(final ByteBuffer nativePtr, final FileOutputStream logStream) {
		super(nativePtr, logStream);
	}

	/**
	 * Create a {@link DefaultHeartbeatPower}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatPower create(final int windowSize) {
		return create(windowSize, null);
	}

	/**
	 * Create a {@link DefaultHeartbeatPower}.
	 * 
	 * @param windowSize
	 * @param logStream
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatPower create(final int windowSize, final FileOutputStream logStream) {
		try {
			final ByteBuffer ptr = HeartbeatPowJNI.get().init(windowSize, getFileDescriptor(logStream));
			if (ptr == null) {
				throw new IllegalStateException("Failed to get heartbeat over JNI");
			}
			return new DefaultHeartbeatPower(ptr, logStream);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to get file descriptor");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime,
			final long startEnergy, final long endEnergy) {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			HeartbeatPowJNI.get().heartbeat(nativePtr, userTag, work, startTime, endTime, startEnergy, endEnergy);
		} finally {
			lock.readLock().unlock();
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		heartbeat(userTag, work, startTime, endTime, 0, 0);
	}

	protected void finishUnchecked() {
		HeartbeatPowJNI.get().finish(nativePtr);
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
				if (HeartbeatPowJNI.get().logHeader(getFileDescriptor(logStream)) != 0) {
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
				if (HeartbeatPowJNI.get().logWindowBuffer(nativePtr, getFileDescriptor(logStream)) != 0) {
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
			return HeartbeatPowJNI.get().getWindowSize(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getUserTag() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getUserTag(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getGlobalTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getWindowTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getGlobalWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getWindowWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getGlobalPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getWindowPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getInstantPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalEnergy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getGlobalEnergy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowEnergy() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getWindowEnergy(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPower() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getGlobalPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPower() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getWindowPower(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPower() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatPowJNI.get().getInstantPower(nativePtr);
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
