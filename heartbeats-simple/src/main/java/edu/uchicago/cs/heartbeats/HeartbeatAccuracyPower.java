package edu.uchicago.cs.heartbeats;

/**
 * Abstraction for Heartbeat-Accuracy-Power implementation over JNI.
 * 
 * @author Connor Imes
 */
public interface HeartbeatAccuracyPower extends HeartbeatAccuracy, HeartbeatPower {

	/**
	 * Issue a heartbeat.
	 * 
	 * @param userTag
	 * @param work
	 * @param startTime
	 * @param endTime
	 * @param accuracy
	 * @param startEnergy
	 * @param endEnergy
	 */
	void heartbeat(long userTag, long work, long startTime, long endTime, long accuracy, long startEnergy,
			long endEnergy);

}
