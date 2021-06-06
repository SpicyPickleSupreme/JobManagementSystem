package jms.system;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jms.config.Configuration;
import jms.enums.JobCode;
import jms.enums.JobState;

public class JobExecutor {
	
	ExecutorService executorService;
	
	/**
	 * Constructor
	 */
	protected JobExecutor() {
		executorService = Executors.newFixedThreadPool(Configuration.ExecutorThreadPoolAmount);
	}
	
	/**
	 * Runs the job, checks to see if the job state has finished, if not proceed.
	 * @param job to be executed
	 */
	protected void executeJob(Job job) {
		int jobResult;
		
		JobState jobState = job.getJobState();
		if(jobState != JobState.QUEUED) return;
		try {
			
			
			job.setJobState(JobState.RUNNING);
			jobResult = job.execute();
			job.setJobResultCode(jobResult);
			job.setJobState(jobResult >= JobCode.SUCCESS ? JobState.SUCCESS : JobState.FAILED);
			
			
		}catch(Exception e) {
			job.setJobState(JobState.FAILED);
		}
	}
	
	/**
	 * Runs jobs asynchronous to prevent a single job from blocking other scheduled jobs
	 * that need to execute.
	 * @param job
	 */
	protected void executeJobAsync(Job job) {
		executorService.submit(()->{
			executeJob(job);
		});
	}
	
	/**
	 * Executes job async, can be given a runnable to execute upon finish
	 * @param job - to be executed
	 * @param onFinish - run operation on finish
	 */
	protected void executeJobAsync(Job job, Runnable onFinish) {
		executorService.submit(()->{
			executeJob(job);
			onFinish.run();
		});
	}
	
	/**
	 * Stops the job executor
	 */
	protected void stop() {
		executorService.shutdown();
	}
	

}
