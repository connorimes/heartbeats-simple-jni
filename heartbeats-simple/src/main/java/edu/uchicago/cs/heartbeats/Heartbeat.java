package edu.uchicago.cs.heartbeats;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Abstraction for Heartbeat implementation over JNI.
 * 
 * You must call {@link #finish()} before dropping the instance and allowing it
 * to be garbage collected.
 * 
 * @author Connor Imes
 */
public interface Heartbeat {

	/**
	 * Issue a heartbeat.
	 * 
	 * @param userTag
	 * @param work
	 * @param startTime
	 * @param endTime
	 * @throws IllegalArgumentException
	 *             if heartbeat fails
	 */
	void heartbeat(long userTag, long work, long startTime, long endTime);

	/**
	 * Free memory allocations, etc.
	 * 
	 * @return 0 on success, failure code otherwise
	 * @throws IllegalStateException
	 *             if already finished
	 */
	int finish();

	/**
	 * Write the header text to a log file.
	 *
	 * @param fos
	 * @throws IOException
	 */
	void logHeader(FileOutputStream fos) throws IOException;

	/**
	 * Logs the circular window buffer up to the current read index.
	 *
	 * @param fos
	 * @throws IOException
	 */
	void logWindowBuffer(FileOutputStream fos) throws IOException;

	/**
	 * Returns the size of the sliding window used to compute the current heart
	 * rate
	 *
	 * @return the size of the sliding window
	 */
	long getWindowSize();

	/**
	 * Returns the current user tag
	 *
	 * @return the current user tag
	 */
	long getUserTag();

	/**
	 * Get the total time for the life of this heartbeat.
	 *
	 * @return the total time
	 */
	long getGlobalTime();

	/**
	 * Get the current window time for this heartbeat.
	 *
	 * @return the window time
	 */
	long getWindowTime();

	/**
	 * Get the total work for the life of this heartbeat.
	 *
	 * @return the total work
	 */
	long getGlobalWork();

	/**
	 * Get the current window work for this heartbeat.
	 *
	 * @return the window work
	 */
	long getWindowWork();

	/**
	 * Returns the performance over the life of the entire application
	 *
	 * @return the performance over the entire life of the application
	 */
	double getGlobalPerf();

	/**
	 * Returns the performance over the last window (as specified to init)
	 * heartbeats
	 *
	 * @return the performance over the last window
	 */
	double getWindowPerf();

	/**
	 * Returns the performance for the last heartbeat.
	 *
	 * @return the performance for the last heartbeat
	 */
	double getInstantPerf();

}
