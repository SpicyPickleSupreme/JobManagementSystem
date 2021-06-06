package jms.system;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jms.enums.JobCode;
import jms.enums.JobState;
import jms.job.Job;

public class JobExecutor {
	
	ExecutorService executorService;
	
	public JobExecutor() {
		executorService = Executors.newFixedThreadPool(5);
	}
	
	/**
	 * Runs the job, checks to see if the job state has finished, if not proceed.
	 * @param job
	 */
	public void executeJob(Job job) {
		int jobResult;
		
		JobState jobState = job.getJobState();
		if(jobState == JobState.FAILED || jobState == JobState.SUCCESS) return;
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
	public void executeJobAsync(Job job) {
		executorService.submit(()->{
			executeJob(job);
		});
	}
	
	public void executeJobAsync(Job job, Runnable onFinish) {
		executorService.submit(()->{
			executeJob(job);
			onFinish.run();
		});
	}
	

}
