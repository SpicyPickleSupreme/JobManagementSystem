package jms.config;

public class Configuration {

	//Amount of threads to pool for the scheduler
	public static final int SchedulerThreadPoolAmount = 10;
	
	/**
	 * This amount is used to determine how many jobs will be
	 * essentially in queue to be ran to prevent a large amount of jobs
	 * taking up space if the time they are to be scheduled is outside the range.
	 */
	public static final int JobBatchTimeRangeSeconds = 300;
	
	
}
