package jms.system;

/**
 * Job Management Service
 * 
 * The goal of this system is to handle the 
 * execution of multiple types of Jobs.
 * 
 * Used for handling the job scheduler, 
 * for executing and scheduling jobs
 * @author Caleb
 *
 */
public class JobManagementService {


	private JobScheduler jobScheduler;
	private static JobManagementService jms = null;
	public static boolean isRunning = false;
	/**
	 * initializes service
	 */
	private static void init() {
		if(jms == null) jms = new JobManagementService();
	}
	
	/**
	 * Constructor, initializes job scheduler
	 */
	private JobManagementService() {
		jobScheduler = new JobScheduler();
	}
	
	/**
	 * Start job scheduler
	 */
	private void startScheduler() {
		jobScheduler.start();
	}
	
	/**
	 * Stop job scheduler
	 */
	private void stopScheduler() {
		jobScheduler.stop();
	}
	
	
	/**
	 * Static method for starting JMS.
	 */
	public static void start() {
		if(jms == null) init();
		jms.startScheduler();
		isRunning = true;
		while(isRunning) {
			try {
				Thread.sleep(10000);
			}catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * Starts JMS without blocking.
	 */
	public static void startAsync() {
		if(jms == null) init();
		
		jms.startScheduler();
		isRunning = true;
	}
	
	/**
	 * Static method for stopping JMS.
	 */
	public static void stop() {
		if(jms != null) {
			jms.stopScheduler();
		}	
		isRunning = false;
	}
	
	/**
	 * Static method for executing a job.
	 * @param job - job to be executed
	 */
	public static void executeJob(Job job) {
		if(jms == null) init();
		
		if(job != null)
			jms.jobScheduler.scheduleJob(job,0);
	}
	
	/**
	 * Static method for scheduling the job at a given time
	 * @param job - Job to be scheduled.
	 * @param time - time to schedule job in milliseconds.
	 */
	public static void scheduleJob(Job job, long time) {
		if(jms == null) init();
		
		if(job != null)
			jms.jobScheduler.scheduleJob(job,time);
	}
	
	/**
	 * Removes job from the queue
	 * @param job job to unschedule
	 * @return true if scheduled, false if not or jms not initialized
	 */
	public static boolean unscheduleJob(Job job) {
		if(jms != null && job != null) {
			return jms.jobScheduler.unqueueJob(job);
		}
		return false;
	}
	
	/**
	 * Gets the amount of jobs scheduled.
	 * @return scheduled amount
	 */
	public static int getScheduledJobCount() {
		if(jms == null) return -1;
		
		return jms.jobScheduler.getQueueSize();
	}
	
	/**
	 * Checks to see if the job provided is next in queue to be ran.
	 * @param job to check
	 * @return true if job is next, false otherwise
	 */
	public static boolean isJobScheduledNext(Job job) {
		return jms.jobScheduler.getNextJob() == job;
	}

}
