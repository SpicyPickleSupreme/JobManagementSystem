package jms.enums;

/**
 * At any one time a Job has one of four states: QUEUED, RUNNING, SUCCESS, FAILED. Following
 * the execution of a Job, it should be left in an appropriate state.
 * @author Caleb Bishop
 *
 */
public enum JobState {

	QUEUED,
	RUNNING,
	SUCCESS,
	FAILED
}
