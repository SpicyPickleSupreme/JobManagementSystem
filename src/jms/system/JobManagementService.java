package jms.system;

import jms.job.Job;

/**
 * Job Management Service
 * 
 * The goal of this system is to handle the 
 * execution of multiple types of Jobs.
 * 
 * Used for handling the job scheduler, 
 * for executing and scheduling jobs
 * @author Caleb Bishop
 *
 */
public class JobManagementService {


	private JobScheduler jobScheduler;
	private static JobManagementService jms = null;
	
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
		if(jms == null) jms = new JobManagementService();
		jms.startScheduler();
	}
	
	/**
	 * Static method for stopping JMS.
	 */
	public static void stop() {
		if(jms != null) {
			jms.stopScheduler();
		}	
	}
	
	/**
	 * Static method for executing a job.
	 * @param job - job to be executed
	 */
	public static void executeJob(Job job) {
		if(jms != null && job != null)
			jms.jobScheduler.scheduleJob(job,0);
	}
	
	/**
	 * Static method for scheduling the job at a given time
	 * @param job - Job to be scheduled.
	 * @param time - time to schedule job in milliseconds.
	 */
	public static void scheduleJob(Job job, long time) {
		if(jms != null && job != null)
			jms.jobScheduler.scheduleJob(job,time);
	}

}
