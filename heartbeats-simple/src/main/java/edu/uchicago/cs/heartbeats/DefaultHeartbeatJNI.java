package edu.uchicago.cs.heartbeats;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Gets a heartbeat implementation and exposes methods for performing operations
 * on it. This implementation is a simple wrapper around the JNI functions.
 * 
 * ttempting to perform operations after {@link #finish()} is called will result
 * in an {@link IllegalStateException}.
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
		this(nativePtr, null);
	}

	/**
	 * Don't allow public instantiation. Should use {@link #create(int)} which
	 * throws exceptions on failure.
	 * 
	 * @param nativePtr
	 * @param logStream
	 */
	protected DefaultHeartbeatJNI(final ByteBuffer nativePtr, final FileOutputStream logStream) {
		super(nativePtr, logStream);
	}

	/**
	 * Create a {@link DefaultHeartbeatJNI}.
	 * 
	 * @param windowSize
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatJNI create(final int windowSize) {
		return create(windowSize, null);
	}

	/**
	 * Create a {@link DefaultHeartbeatJNI}.
	 * 
	 * @param windowSize
	 * @param logStream
	 * @throws IllegalStateException
	 *             if native resources cannot be allocated
	 */
	public static DefaultHeartbeatJNI create(final int windowSize, final FileOutputStream logStream) {
		try {
			final ByteBuffer ptr = HeartbeatJNI.get().heartbeatInit(windowSize, getFileDescriptor(logStream));
			if (ptr == null) {
				throw new IllegalStateException("Failed to get heartbeat over JNI");
			}
			return new DefaultHeartbeatJNI(ptr, logStream);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to get file descriptor");
		}
	}

	public void heartbeat(final long userTag, final long work, final long startTime, final long endTime) {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			HeartbeatJNI.get().heartbeat(nativePtr, userTag, work, startTime, endTime);
		} finally {
			lock.readLock().unlock();
		}
	}

	protected void finishUnchecked() {
		HeartbeatJNI.get().heartbeatFinish(nativePtr);
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
				if (HeartbeatJNI.get().heartbeatLogHeader(getFileDescriptor(logStream)) != 0) {
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
				if (HeartbeatJNI.get().heartbeatLogWindowBuffer(nativePtr, getFileDescriptor(logStream)) != 0) {
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
			return HeartbeatJNI.get().heartbeatGetWindowSize(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getUserTag() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatJNI.get().heartbeatGetUserTag(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatJNI.get().heartbeatGetGlobalTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowTime() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatJNI.get().heartbeatGetWindowTime(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getGlobalWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatJNI.get().heartbeatGetGlobalWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getWindowWork() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatJNI.get().heartbeatGetWindowWork(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getGlobalPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatJNI.get().heartbeatGetGlobalPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getWindowPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatJNI.get().heartbeatGetWindowPerf(nativePtr);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getInstantPerf() {
		try {
			lock.readLock().lock();
			enforceNotFinished();
			return HeartbeatJNI.get().heartbeatGetInstantPerf(nativePtr);
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
