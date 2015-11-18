package edu.uchicago.cs.heartbeats;

/**
 * Abstraction for Heartbeat-Power implementation over JNI.
 * 
 * @author Connor Imes
 */
public interface HeartbeatPower extends Heartbeat {

	/**
	 * Issue a heartbeat.
	 * 
	 * @param userTag
	 * @param work
	 * @param startTime
	 * @param endTime
	 * @param startEnergy
	 * @param endEnergy
	 * @throws IllegalArgumentException
	 *             if heartbeat fails
	 */
	void heartbeat(long userTag, long work, long startTime, long endTime, long startEnergy, long endEnergy);

	/**
	 * Get the total energy for the life of this heartbeat.
	 *
	 * @return the total energy
	 */
	long getGlobalEnergy();

	/**
	 * Get the current window energy for this heartbeat.
	 *
	 * @return the window energy
	 */
	long getWindowEnergy();

	/**
	 * Returns the power over the life of the entire application
	 *
	 * @return the power over the entire life of the application
	 */
	double getGlobalPower();

	/**
	 * Returns the power over the last window (as specified to init) heartbeats
	 *
	 * @return the power over the last window
	 */
	double getWindowPower();

	/**
	 * Returns the power for the last heartbeat.
	 *
	 * @return the power for the last heartbeat
	 */
	double getInstantPower();

}
