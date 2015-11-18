package edu.uchicago.cs.heartbeats;

/**
 * Abstraction for Heartbeat-Accuracy implementation over JNI.
 * 
 * @author Connor Imes
 */
public interface HeartbeatAccuracy extends Heartbeat {

	/**
	 * Issue a heartbeat.
	 * 
	 * @param userTag
	 * @param work
	 * @param startTime
	 * @param endTime
	 * @param accuracy
	 * @throws IllegalArgumentException
	 *             if heartbeat fails
	 */
	void heartbeat(long userTag, long work, long startTime, long endTime, long accuracy);

	/**
	 * Get the total accuracy for the life of this heartbeat.
	 *
	 * @return the total accuracy
	 */
	long getGlobalAccuracy();

	/**
	 * Get the current window accuracy for this heartbeat.
	 *
	 * @return the window accuracy
	 */
	long getWindowAccuracy();

	/**
	 * Returns the accuracy rate over the life of the entire application
	 *
	 * @return the accuracy rate over the entire life of the application
	 */
	double getGlobalAccuracyRate();

	/**
	 * Returns the accuracy rate over the last window (as specified to init)
	 * heartbeats
	 *
	 * @return the accuracy rate over the last window
	 */
	double getWindowAccuracyRate();

	/**
	 * Returns the accuracy rate for the last heartbeat.
	 *
	 * @return the accuracy rate for the last heartbeat
	 */
	double getInstantAccuracyRate();

}
